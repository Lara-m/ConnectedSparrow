# ConnectedSparrow

Hey Cam!

So, to start, look into the SparrowConnect folder. There you'll see a client and a server. 

Server is for the app that's on the drone. Client is for user's app.

Relevant to that, Test2 is the Client app and Test2p (Which was supposed to stand for peer :P ) is for the server. The only thing is hardcoding of the mac address. You can use the app I assembled in the wifidirect to get the mac address. It's just because the app on the drone does not need to check or to connect to anything else. 

Also, peer the two phones before you start.

For peering, go to wifi-direct section of the phones and activate it, and then peer one up with the other phone.

I tried my best to get minimum functionality to get this thing going.

Hope that works.

Cheers!

_P.S. If you changed the socket port at one, make sure to use the exact same on another. That's why it wasn't running the other day._