pragma solidity ^0.4.0;

contract ClientReceipt {
    event Deposit(
        address from,
        address to,
        string abc
    );

    struct OrgInfo{
        string owner;
        uint256  age;
    }

    function deposit() {
        // Any call to this function (even deeply nested) can
        // be detected from the JavaScript API by filtering
        // for `Deposit` to be called.
        Deposit(msg.sender,msg.sender,"abc");
    }


    function queryInfo()public returns (string info){
       info = "abcd";
    }
}