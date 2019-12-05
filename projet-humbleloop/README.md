# projet-humbleloop
projet-humbleloop created by GitHub Classroom


Phine Loops Project for Dauphine-java-M1

Nguyen Philippe
Nourry Charles

Project implement for javaavance course, University of Paris-Dauphine

Generator, Checker, Solver for Phine Loops puzzles.
Resolution of puzzle's problems, using Constraint Satisfaction Problem
Graphical User Interface with Vaadin framework

mvn install

(project)

cd humbleloop

mvn package

java -jar target/hey-jar-with-dependencies.jar -g 10x10 --output file.txt

java -jar target/hey-jar-with-dependencies.jar -s file.txt --output resolved.txt

java -jar target/hey-jar-with-dependencies.jar -c resolved.txt

(GUI)

cd app

mvn jetty:run
