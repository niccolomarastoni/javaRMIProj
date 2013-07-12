	keytool -genkey -keyalg RSA -keystore server.keystore -dname "CN=Server, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass server -keypass server
	keytool -genkey -keyalg RSA -keystore client.keystore -dname "CN=Client, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass client -keypass client
	keytool -export -rfc -keystore server.keystore -alias mykey -file server.public-key -storepass server
	keytool -export -rfc -keystore client.keystore -alias mykey -file client.public-key -storepass client
	keytool -import -trustcacerts -alias client -keystore server.keystore -file client.public-key -storepass server
	keytool -import -trustcacerts -alias server -keystore client.keystore -file server.public-key -storepass client
