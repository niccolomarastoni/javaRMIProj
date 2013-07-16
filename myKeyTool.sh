keytool -genkey -keyalg RSA -keystore authServer.keystore -dname "CN=authServer, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass authServer -keypass authServer
keytool -genkey -keyalg RSA -keystore loginClient.keystore -dname "CN=loginClient, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass loginClient -keypass loginClient
keytool -export -rfc -keystore authServer.keystore -alias mykey -file authServer.public-key -storepass authServer
keytool -export -rfc -keystore loginClient.keystore -alias mykey -file loginClient.public-key -storepass loginClient
keytool -import -trustcacerts -alias loginClient -keystore authServer.keystore -file loginClient.public-key -storepass authServer
keytool -import -trustcacerts -alias authServer -keystore loginClient.keystore -file authServer.public-key -storepass loginClient
