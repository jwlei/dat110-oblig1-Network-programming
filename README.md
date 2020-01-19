## Week 4 Project : 28.01 - 01.02

### Organisation

Week 4 is devoted to project work which is to be undertaken in **groups of 2-3 students**.

There will be no lecture and no lab Wednesday/Thursday due to travelling. The lecturers and teaching assistants will be available on Monday 28.1 and on Friday 1.2 in the normal lecture and lab-hours.

You are strongly encouraged to use the [discussion forum](https://hvl.instructure.com/courses/6156/discussion_topics/30353) in Canvas throughout the week.

### Overview

The project builds on socket programming and network applications and aims to consolidate important concepts that have been covered until now in the course: layering, services, protocol design, headers, encapsulation/decapsulation, remote procedure calls (RPC), and marshalling/unmarshalling of parameters.

The project is comprised of three main tasks

- implementation of a messaging layer on top of TCP sockets for exchanging short messages between a messaging client and a messaging server
- implementation of a light-weight RPC layer/middleware on top of the messaging layer
- application of the RPC layer for realising an small IoT network application comprised of a sensor, and display, and a controller

### Getting Started

You should start by cloning the Java code which can be found in the github repository

https://github.com/selabhvl/dat110-project1-startcode.git

which contains an Eclipse-project with start-code. In addition, it also contains a number of unit tests which can be used for some basic testing of the implemented functionality.

It should not be necessary to add additional classes in order to complete the project and the unit-tests should not be modified/removed as they will be used for evaluation of the submitted solution.

In order for the group to use their own git-repository for the further work on the codebase, one member of the group must create an empty repository on github/bitbucket without a README file and without a `.gitignore` file, and then perform the following operations

`git remote remove origin`

`git remote add origin <url-to-new-empty-repository>`

`git push -u origin master`

The other group members can now clone this new repository and work with a shared repository as usual.

### Taks 1: Messaging layer

The messaging layer is to be implemented on top of TCP sockets and provide a service for connection-oriented, reliable, and bidirectional exchange of (short) messages carrying up to 127 bytes of data/payload.

The messaging layer is to be based on a client-server architecture supporting a client in establishing a connection to a server on top of which the messages can be exchanged.

The messaging protocol is based on sending segments of 128 bytes on the underlying TCP connection such that the first byte of the segment is to be interpreted as an integer in the range 0..127 specifying how many of the subsequent 127 bytes is payload data. Any remaining bytes is simply considered to be padding and can be ignored.

The implementation of the messaging service is to be located in the `no.hvl.dat110.messaging` package.

You are required to implement the methods marked with `TODO` in the following classes

- `Message.java` implementing methods for encapsulation and decapsulation of payload data according to the segment format described above.

- `Connection.java` implementing the connection abstraction linking the connection to the underlying TCP socket and associated input and output data streams that is to be used for sending and receiving message.

- `MessagingClient.java` implementing the methods for the client-side of the messaging service and responsible for creating the underlying TCP socket on the client-side.

- `MessagingServer.java` implementing the methods for the server-side of the messaging service. In the current project, a server is only required to handle a single connection to a client.

Unit-tests for the messaging layer can be found in the `no.hvl.dat110.messaging.tests` package.

**Optional challenge:** If you have time, you may consider implementing a messaging protocol that supports the exchange of arbitrarily long messages and without the use of padding.

### Task 2: RPC layer

The description of this task assumes that you have read Chap. 4.2 (Remote Procedure Call) in the distributed systems book.

In this task you will implement a light-weight RPC middleware on top of the messaging layer. The RPC layer is also based on a client-server architecture in which the client-side is able to perform remote procedure calls on objects located on the server-side.

The RPC middleware is light-weight in that only the types `void`, `String`, `int`, and `boolean` is supported as parameter and return types. Furthermore, the methods supported can have at most one parameter. Furthermore, the middleware does not support automatic generation of stub code and the marshalling and unmarshalling of parameters and return values. The (un)marshalling will have to be implemented manually by the developer using the RPC middleware. Finally, it is assumed that the marshalled parameter and return values can be represented using at most 127 bytes.

To perform a call, the client-side stub must send a request message containing first a byte specifying the identifier of the remote procedure call to be invoked on the server-side. The subsequent bytes in the request is then a sequence of bytes resulting from the marshalling representing the parameter (if any). When receiving the request, the server-side uses the identifier to perform a look-up in a table to find the RPC method to invoke. Before invoking the method, the parameter (if any) must be unmarshalled on the server-side. After having the invoked the method, any return value must be marshalled and then sent back to the client-side in a reply message where the first byte (again) specifies the executed method. Finally, the client-side have to unmarshall the return value (if any).

The implementation of the RPC layer is to be located in the `no.hvl.dat110.rpc` package. You are required to provide the missing methods implementations in the following classes

- `RPCUtils.java` containing utility methods for the unmarshalling and marshalling of the data types supported. The implementation of the marshalling/unmarshalling of `booleans` is provided and can be used for inspiration. Remember that an integer in Java is 4 bytes.

- `RPCClient.java` implementing the client-side of the RPC layer using the client-side of the underlying messaging layer for communication.

- `RPCServer.java` implementing the server-side of the RPC layer using the server-side of the underlying messaging layer for communication. The server also contains a hash-map which is used to register classes containing methods for remote invocation.

Unit-tests for the RPC utilities can be found in the `TestRPCUtils.java` class and Unit-tests testing the remote procedure call mechanism can be found in the `TestRPC.java` class.  

In addition to the three classes above, the messaging layer contains the following

- `RPCImpl.java` specifying an interface containing an `invoke` method that any server-side class exposing a remote method must implement. This `invoke` method should handle the unmarshalling of the parameters, then call the real underlying remote method implementation, and finally marshall the return value. It is this `invoke`-method that the RPC server will call in order to have the RPC call executed.

- `RPCStub.java` implementing a `register` method that allows a client-side stub to be registered in the RPC middleware. Any client-side stub must extend this class and implement the client-side stub.

- `RPCServerStopImpl.java` implementing the server-side of a remote method `void stop()` which the client-side can use to terminate the server. The class illustrates the server-side implementation of an RPC method and how first parameters must be unmarshalled, then the underlying method called, and then the marshalling of the return value.

- `RPCServerStopStub.java` implementing the client-side stub of the remote method `void stop()`. The class illustrates the client-side implementation of an RPC method illustrating how first parameters are marshalled, then the RPC layer is asked to executed the call, and finally the return must be unmarshalled.

The `void stop()` method uses RPC identifier 0 and this (reserved) identifier should not be used when implementing other RPC methods using the RPC layer.

** Optional challenges: ** If you have time, you may consider implement an RPC layer where methods can have more than a single parameter. Also, you may investigate how to implement the automatic generation of the client-side and server-side stub-code which would be a first step towards supporting arbitrary Java-objects as parameter and return types. Finally, you may consider making the RPC server multi-threaded such that multiple simultaneous clients can be handled.

### Task 3: Using the RPC layer for an IoT network application

In this task you will use the RPC layer to implement a small IoT system comprised of a controller, a (temperature) sensor, and a display. The controller should play the role of an RPC client while the sensor and display plays the role of RPC servers.

The controller should regular retrieve the current temperature using a `int read()` RPC call on the sensor and then use a `void write(String str)` RPC call on the display the current temperature.

#### Controller implementation

The implementation of the controller is to be provided in the `no.dat110.system.controller` package. You must implement the code missing in the following classes

- `Display.java` - here you have to implement the client-side stub of the  `void write(String str)` RPC method.

- `Sensor.java` - here you have to implement the client-side stub for the `int read()` RPC method.

- `Controller.java` - here you have to implement the creation of the client-side stubs and the registration of the stubs in the middleware. Finally, the controller must connect to the sensor and display RPC servers and implement a bounded-loop in which the temperature is retrieved from the sensor and shown on the display.

#### Display implementation

The implementation of the display is in the `no.hvl.dat110.system.display` package. You must implement the server-side of the `write` RPC method in the `DisplayImpl.java` and the display server in `DisplayDevice.java` class.  You may use the sensor server implementation for inspiration.

#### Sensor implementation

The implementation of the sensor is in the `no.hvl.dat110.system.sensor` package.

If everything has been implemented correctly, you should not be able to start the display-device and sensor-device, and then the controller and see the reporting temperatures in the console. The test in `TestSystem.java` contains a test that runs all devices within the same JVM using threads. You can the devices in separate JVMs by running the individual devices as a Java application (they each have a main method).

### Handing in the project

Each group must hand in a link on Canvas to a git-repository containing their implementation. You should keep the unit-test in the project as they are as we will use these for testing your implementation.

Please remember to hand-in as a member of a group in Canvas: https://hvl365-my.sharepoint.com/:w:/g/personal/akv_hvl_no/EdkQXNKVjmhPrHNtD3n5r74B6KSb7DwmVYf9MA3SIUA4Sw?e=hC5Q9i
