package annchain.genesis.sdk.core.utils;


public class ResultCode {

    public static final int CodeType_OK = 0;
    public static final int CodeType_InternalError      = 1;
    public static final int CodeType_EncodingError      = 2;
    public static final int CodeType_BadNonce           = 3;
    public static final int CodeType_Unauthorized       = 4;
    public static final int CodeType_InsufficientFunds  = 5;
    public static final int CodeType_UnknownRequest     = 6;
    public static final int CodeType_InvalidTx          = 7;
    public static final int CodeType_Timeout  = 8;
    public static final int CodeType_NonceTooLow        = 9;
    public static final int CodeType_BaseDuplicateAddress     = 101;
    public static final int CodeType_BaseEncodingError        = 102;
    public static final int CodeType_BaseInsufficientFees     = 103;
    public static final int CodeType_BaseInsufficientFunds    = 104;
    public static final int CodeType_BaseInsufficientGasPrice = 105;
    public static final int CodeType_BaseInvalidInput         = 106;
    public static final int CodeType_BaseInvalidOutput        = 107;
    public static final int CodeType_BaseInvalidPubKey        = 108;
    public static final int CodeType_BaseInvalidSequence      = 109;
    public static final int CodeType_BaseInvalidSignature     = 110;
    public static final int CodeType_BaseUnknownAddress       = 111;
    public static final int CodeType_BaseUnknownPubKey        = 112;
    public static final int CodeType_BaseUnknownPlugin        = 113;
    public static final int CodeType_WrongRLP                 = 114;
    public static final int CodeType_SaveFailed               = 115;
    public static final int CodeType_WrongJSON                = 116;
    public static final int CodeType_GovUnknownEntity       = 201;
    public static final int CodeType_GovUnknownGroup        = 202;
    public static final int CodeType_GovUnknownProposal     = 203;
    public static final int CodeType_GovDuplicateGroup      = 204;
    public static final int CodeType_GovDuplicateMember     = 205;
    public static final int CodeType_GovDuplicateProposal   = 206;
    public static final int CodeType_GovDuplicateVote       = 207;
    public static final int CodeType_GovInvalidMember       = 208;
    public static final int CodeType_GovInvalidVote         = 209;
    public static final int CodeType_GovInvalidVotingPower  = 210;
    public static final int CodeType_ErrorParam  = 301;
}
