package com.genesis.api.bean.genesis;

import lombok.Data;

@Data
public class ResultBlock {
	
	private BlockMeta block_meta;
	private Block block;

    @Data
    public static class BlockMeta {
        private String hash; 
        private Header header;
        private PartSetHeader parts_header;
    }
    
    @Data
    public static class Block {
    	private Header header;
        private TxData data;
        private LastCommit last_commit;
    }
    
    @Data
    public static class Header {
        private String chain_id;
        private String height;
        private String time;
        private long num_txs;
        private BlockID last_block_id;
        private String last_commit_hash;
        private String data_hash;
        private String validators_hash;
        private String app_hash;
        private String recepits_hash;
        private String proposer_address;
    }
    
    @Data
    public static class BlockID {
    	private PartSetHeader parts;
        private String hash;
    }
    
    @Data
    public static class PartSetHeader {
    	private int total;
        private String hash;
    }
    
    @Data
    public static class TxData {
    	private String[] txs;
    	private String[] extxs;
        private String hash;
    }
    
    @Data
    public static class LastCommit {
    	private BlockID blockID;
    	private Vote[] precommits;
    }
    
    @Data
    public static class Vote {
    	private String validator_address;
    	private int validator_index;
    	private String height;
    	private long round;
    	private byte type;
    	private BlockID block_id;
        private String signature;
    }
    
    @Data
    public static class Mutex{
    	private int state;
    	private int sema;
    }
}
