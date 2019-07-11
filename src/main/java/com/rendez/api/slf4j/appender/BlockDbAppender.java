package com.rendez.api.slf4j.appender;

import com.rendez.api.CryptoUtil;
import com.rendez.api.NodeSrv;
import com.rendez.api.TransactionUtil;
import com.rendez.api.blockdb.BlockDbTransaction;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.PrivateKeyECDSA;
import com.rendez.api.crypto.Signature;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class BlockDbAppender extends org.apache.log4j.AppenderSkeleton
        implements org.apache.log4j.Appender {


    /**
     * size of LoggingEvent buffer before writting to the blockdb.
     * Default is 1.
     */
    protected int bufferSize = 1;
    /**
     * ArrayList holding the buffer of Logging Events.
     */
    protected ArrayList buffer;
    /**
     * Helper object for clearing out the buffer
     */
    protected ArrayList removes;

    /**
     * blockdb service node
     */
    private NodeSrv nodeSrv;
    /**
     * blockdb node url,default http://localhost:26657
     */
    private String nodeUrl = "http://localhost:26657";

    private boolean locationInfo = false;
    /**
     * the chain account private key
     */
    private String priKey;
    /**
     * the layout pattern for blockDB appender
     */
    private String pattern;

    public BlockDbAppender() {
        super();
        buffer = new ArrayList(bufferSize);
        removes = new ArrayList(bufferSize);
    }

    @Override
    protected void append(LoggingEvent event) {
        event.getNDC();
        event.getThreadName();
        // Get a copy of this thread's MDC.
        event.getMDCCopy();
        if (locationInfo) {
            event.getLocationInformation();
        }
        event.getRenderedMessage();
        event.getThrowableStrRep();
        buffer.add(event);

        if (buffer.size() >= bufferSize)
            flushBuffer();
    }


    /**
     * loops through the buffer of LoggingEvents, gets a
     * sql string from getLogStatement() and sends it to execute().
     * Errors are sent to the errorHandler.
     * <p>
     * If a statement fails the LoggingEvent stays in the buffer!
     */
    public void flushBuffer() {
        //Do the actual logging
        removes.ensureCapacity(buffer.size());
        for (Iterator i = buffer.iterator(); i.hasNext(); ) {
            LoggingEvent logEvent = (LoggingEvent) i.next();
            try {
                String info = getInfo(logEvent);
                execute(info);
            } catch (Exception e) {
                errorHandler.error("Failed to commit to blockdb", e,
                        ErrorCode.FLUSH_FAILURE);
            } finally {
                removes.add(logEvent);
            }
        }

        // remove from the buffer any events that were reported
        buffer.removeAll(removes);

        // clear the buffer of reported events
        removes.clear();
    }

    private void execute(String info) {
        nodeSrv = null;
        try {
            nodeSrv = getNodeSrv();
            PrivateKey prk = new PrivateKeyECDSA(getPriKey());
            //assemble transaction entity
            BlockDbTransaction transaction = new BlockDbTransaction(prk.getAddress(), BigInteger.valueOf(System.currentTimeMillis()), info.getBytes());
            //signature transaction
            Signature signature = CryptoUtil.generateBlockSignature(transaction, prk);
            //encode tx with signature
            byte[] message = TransactionUtil.encodeWithSig(transaction, signature);
            String txHash = CryptoUtil.txHash(transaction);

            System.out.println("--txHash--: " + txHash);
            //submit to block chain
            nodeSrv.sendTxToNode(message, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private NodeSrv getNodeSrv() {
        if (nodeSrv == null) {
            nodeSrv = new NodeSrv(getNodeUrl());
        }
        return nodeSrv;
    }

    private String getInfo(LoggingEvent event) {
        if (getLayout() == null) {
            PatternLayout pl = new PatternLayout();
            pl.setConversionPattern(getPattern());
            setLayout(pl);
        }

        return getLayout().format(event);
    }

    @Override
    public void close() {
        flushBuffer();

        this.closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public boolean isLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
