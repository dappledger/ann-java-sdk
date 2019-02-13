package annchain.genesis.sdk.core.protocol.core.methods.request;


public class NodeUpdate {
    /**
     * 升级:isCA=true
     */
    private String isCA;
    private String pubkey;
    private String sigs;
    /**
     * 升级:opcode=1
     */
    private String opcode;
    private String rpc_address;

    public String getIsCA() {
        return isCA;
    }

    public void setIsCA(String isCA) {
        this.isCA = isCA;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public String getSigs() {
        return sigs;
    }

    public void setSigs(String sigs) {
        this.sigs = sigs;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getRpc_address() {
        return rpc_address;
    }

    public void setRpc_address(String rpc_address) {
        this.rpc_address = rpc_address;
    }

    public NodeUpdate() {
    }

    public NodeUpdate(String isCA, String pubkey, String sigs, String opcode, String rpc_address) {
        this.isCA = isCA;
        this.pubkey = pubkey;
        this.sigs = sigs;
        this.opcode = opcode;
        this.rpc_address = rpc_address;
    }

    @Override
    public String toString() {
        return "{" +
                "\"isCA\":\"" + isCA + "\"" +
                ",\"pubkey\":\"" + pubkey + "\"" +
                ",\"sigs\":\"" + sigs + "\"" +
                ",\"opcode\":\"" + opcode + "\"" +
                ",\"rpc_address\":\"" + rpc_address + "\"" +
                "}";
    }
}
