package fr.dauphine.javaavance.phineloops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Philippe on 27/12/2017.
 *
 * @author Philippe
 */
public class Square extends Shape {

  Square(int type, int offset) {
    super(4);

    setOffset(offset);

    setType(type);

    switch (type) {
      case 0:
        connections = new ArrayList<>(Arrays.asList(false, false, false, false));
        break;
      case 1:
        connections = new ArrayList<>(Arrays.asList(true, false, false, false));
        break;
      case 2:
        connections = new ArrayList<>(Arrays.asList(true, false, true, false));
        break;
      case 3:
        connections = new ArrayList<>(Arrays.asList(true, true, false, true));
        break;
      case 4:
        connections = new ArrayList<>(Arrays.asList(true, true, true, true));
        break;
      case 5:
        connections = new ArrayList<>(Arrays.asList(true, true, false, false));
        break;
    }
  }

  @Override
  public void findType(){//find the type from the array of connections
    int numOfConnections = 0;
    int i;
    for (i = 0; i < connections.size(); i++) {
      if(connections.get(i)){
        numOfConnections++;
      }
    }
    switch (numOfConnections){
      case 0:
        this.type = 0;
        break;
      case 1:
        this.type = 1;
        break;
      case 3:
        this.type = 3;
        break;
      case 4:
        this.type = 4;
        break;
      case 2:
        i=0;
        while(!connections.get(i)){
          i++;
        }
        if (connections.get(i+1)||(i==0 && connections.get(3))){
          this.type = 5;
          break;
        }
        this.type = 2;
        break;
    }
  }
  @Override
  public void setRandomOffset(){//set a random offset from the type
    Random random = new Random();
    switch (type){
      case 0:
        offset = 0;
        break;
      case 1:
        offset = random.nextInt(4);
        break;
      case 2:
        offset = random.nextInt(2);
        break;
      case 3:
        offset = random.nextInt(4);
        break;
      case 4:
        offset = 0;
        break;
      case 5:
        offset = random.nextInt(4);
        break;
    }
  }


  @Override
  public String toString() {
    switch (type) {
      case 0:
        return " ";
      case 1:
        switch (offset) {
          case 0:
            return "\u2579";
          case 1:
            return "\u257A";
          case 2:
            return "\u257B";
          case 3:
            return "\u2578";
        }
      case 2:
        switch (offset) {
          case 0:
            return "\u2503";
          case 1:
            return "\u2501";
        }
      case 3:
        switch (offset) {
          case 0:
            return "\u253B";
          case 1:
            return "\u2523";
          case 2:
            return "\u2533";
          case 3:
            return "\u252B";
        }
      case 4:
        return "\u254B";
      case 5:
        switch (offset) {
          case 0:
            return "\u2517";
          case 1:
            return "\u250F";
          case 2:
            return "\u2513";
          case 3:
            return "\u251B";
        }
    }
    return null;
  }
}
