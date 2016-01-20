Embedded RocketMQ Server
==========================================

## Introduction

This module is to help set up unit or integration testing environment. Namely, you just need one line of Java
code to set up name server and broker on your local machine box.

``
com.ndpmedia.rocketmq.test.Launcher.main(null);
``

__Note__ The broker allows automatically creation of topics and consumer groups.
__Note__ The Name Server listens on localhost:9876 and broker on localhost:10911
    


## How to use
 
 The test code includes a demonstration usage of this module. You are strongly suggested to read it first before
 writing your own test cases.
 
## How to contribute
 
  Be there any issue, please send us a pull request. We will appreciate this very much.
