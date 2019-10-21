pragma solidity ^0.5.0;

contract JavaContractTest {
    event Deposit(
        address owner,
        uint256 age,
        string comment
    );
    
    mapping (address=>uint256) OrgInfo;
    
    constructor() public {
    }

    function deposit(uint256 _age,string memory _comment) public {
        OrgInfo[msg.sender] = _age;
        emit Deposit(msg.sender,_age,_comment);
    }


    function queryInfo() public view returns(uint256){
    	return OrgInfo[msg.sender];
    }
    
    function queryInfoWithAddress(address _owner) public view returns(uint256){
    	return OrgInfo[_owner];
    }
}