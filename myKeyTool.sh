<<<<<<< HEAD
#	keytool -genkey -keyalg RSA -keystore authServer.keystore -dname "CN=authServer, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass server -keypass authServer
#	keytool -genkey -keyalg RSA -keystore loginClient.keystore -dname "CN=loginClient, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass loginClient -keypass loginClient
#	keytool -export -rfc -keystore authServer.keystore -alias pongkey -file authServer.public-key -storepass authServer
#	keytool -export -rfc -keystore loginClient.keystore -alias pongkey -file loginClient.public-key -storepass loginClient
#	keytool -import -trustcacerts -alias loginClient -keystore authServer.keystore -file loginClient.public-key -storepass authServer
#	keytool -import -trustcacerts -alias authServer -keystore loginClient.keystore -file authServer.public-key -storepass loginClient
	keytool -genkey -keyalg RSA -keystore server.keystore -dname "CN=Server, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass server -keypass server
=======
  keytool -genkey -keyalg RSA -keystore server.keystore -dname "CN=Server, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass server -keypass server
>>>>>>> ad15281f9e80ae41f28e17530f739e72e9ebf2bf
	keytool -genkey -keyalg RSA -keystore client.keystore -dname "CN=Client, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass client -keypass client
	keytool -export -rfc -keystore server.keystore -alias mykey -file server.public-key -storepass server
	keytool -export -rfc -keystore client.keystore -alias mykey -file client.public-key -storepass client
	keytool -import -trustcacerts -alias client -keystore server.keystore -file client.public-key -storepass server
	keytool -import -trustcacerts -alias server -keystore client.keystore -file server.public-key -storepass client
