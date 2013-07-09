cd tetraPong
javac *.java
cd ..

rmic -d /home/accounts/studenti/id284txe/public_html/common/ tetraPong.AutenticationServer
rmic -d /home/accounts/studenti/id284txe/public_html/common/ tetraPong.MainServer

#mv "find ./tetraPong/ -type f -name '*'.class | grep -vw "Setup.class""  /home/accounts/studenti/id284txe/public_html/common/tetraPong/ 
cd tetraPong

mv  $(find . -type f -name '*'.class | grep -vw "Setup.class")  /home/accounts/studenti/id284txe/public_html/common/tetraPong/

cd ..

java -classpath :/home/accounts/studenti/id284txe/public_html/common/ tetraPong.Setup 
