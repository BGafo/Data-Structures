import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 *
 * @author Emrah Yilgen
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 * 
 */
public class Transit {
	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 * @return The zero node in the train layer of the final layered linked list
	 */
	public static TNode makeList(int[] trainStations, int[] busStops, int[] locations) {
		TNode trainHead = new TNode();
		TNode busHead = new TNode();
		TNode walkingHead = new TNode();
		TNode trainPtr = trainHead;
		TNode trainPrev = null;
		TNode busPtr = busHead;
		TNode busPrev = null;
		TNode walkingPtr = walkingHead;
		TNode walkingPrev = null;

		trainHead.next = null;
		busHead.next = null;
		walkingHead.next = null;
		trainHead.down = busHead;
		busHead.down = walkingHead;
		walkingHead.down = null;

		for(int i = 0; i<locations.length; i++) {
			if(i == 0){
				walkingPrev = walkingPtr;
				walkingPtr = new TNode(locations[i], null, null);
				walkingHead.next = walkingPtr;
				walkingPrev.next = walkingPtr;
			} else {
			walkingPrev = walkingPtr;
			walkingPtr = new TNode(locations[i], null, null);
			walkingPrev.next = walkingPtr;
			}
		}
		for(int i = 0; i<busStops.length; i++){
			if(i == 0){
			int bStop = busStops[i];
			busPrev = busPtr;
			busPtr = new TNode(bStop, null, null);
			busHead.next = busPtr;
			busPrev.next = busPtr;
			busPtr.down = downConnection(bStop, walkingHead);
			} else {
				int bStop = busStops[i];
				busPrev = busPtr;
				busPtr = new TNode(bStop, null, null);
				busPrev.next = busPtr;
				busPtr.down = downConnection(bStop, walkingHead);
			}
		}
		for(int i = 0; i<trainStations.length; i++){
			if(i == 0){
			int tStop = trainStations[i];
			trainPrev = trainPtr;
			trainPtr = new TNode(tStop, null, null);
			trainHead.next = trainPtr;
			trainPrev.next = trainPtr;
			trainPtr.down = downConnection(tStop, busHead);
			} else{
				int tStop = trainStations[i];
				trainPrev = trainPtr;
				trainPtr = new TNode(tStop, null, null);
				trainPrev.next = trainPtr;
				trainPtr.down = downConnection(tStop, busHead);
			}
		}
		return trainHead;
	}
	
	/**
	 * Modifies the given layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param station The location of the train station to remove
	 */
	public static void removeTrainStation(TNode trainZero, int station) {
		TNode prev = trainZero;
		for(TNode ptr = trainZero; ptr != null; ptr = ptr.next){
			if(ptr.location == station && ptr.next == null){
				ptr.down = null;
				prev.next = null;
			}
				else if(ptr.location == station){
					ptr.down = null;
					prev.next = ptr.next;
				}
					prev = ptr;
				
			}
		}

	/**
	 * Modifies the given layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param busStop The location of the bus stop to add
	 */
	public static void addBusStop(TNode trainZero, int busStop) {
		TNode busPtr = trainZero.down;
		TNode busPrev = trainZero.down;
		TNode walkPtr;
		int walkLastNode = 0;
		while(busPtr != null && busPrev.next.down != null){
			if(busPtr.next == null && busStop > busPtr.location){
				walkPtr = busPtr.down;
				while(walkPtr != null && walkPtr.next != null){
					walkPtr = walkPtr.next;
					walkLastNode = walkPtr.location;
				}
				if(walkLastNode < busStop){
					break;
				}
			}
			if(busPtr.location > busStop && busPrev.location < busStop){
				TNode newBusStop = new TNode(busStop, busPtr, null);
				busPrev.next = newBusStop;
				newBusStop.down = downConnection(busStop, trainZero.down.down);
			} else if(busPtr.location<busStop && busPtr.next == null){
				TNode newBusStop = new TNode(busStop, null, null);
				busPtr.next = newBusStop;
				newBusStop.down = downConnection(busStop, trainZero.down.down);
				} 
			
			busPrev = busPtr;
			busPtr = busPtr.next;
			}
		}

	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param destination An int representing the destination
	 * @return
	 */
	public static ArrayList<TNode> bestPath(TNode trainZero, int destination) {
		ArrayList<TNode> calcPath = new ArrayList<TNode>();
		TNode trainPtr = trainZero;
		TNode trainPrev = trainZero;
		TNode busPtr = trainZero.down;
		TNode busPrev = trainZero.down;
		TNode walkPtr = trainZero.down.down;
		TNode walkPrev = trainZero.down.down;
		if(destination == 1 && trainZero.next.location != 1 && busPtr.next.location != 1){
			calcPath.add(trainPtr);
			calcPath.add(busPtr);
			calcPath.add(walkPtr);
			calcPath.add(walkPtr.next);
			return calcPath;
		} 
		while(trainPtr != null){
			if(trainPtr.location == destination){
				calcPath.add(trainPtr);
				calcPath.add(trainPtr.down);
				calcPath.add(trainPtr.down.down);
				return calcPath;
			} else if(trainPtr.location < destination){
				calcPath.add(trainPtr);

				trainPrev = trainPtr;
				trainPtr = trainPtr.next;
				busPrev = trainPrev.down; 
				busPtr = trainPrev.down;
			} else {
				busPrev = trainPrev.down;
				busPtr = trainPrev.down;
				break;
			}   
		}
		while(busPtr != null){
			if(busPtr.location == destination){
				calcPath.add(busPtr);
				calcPath.add(busPtr.down);
				return calcPath;
			} else if(busPtr.location < destination){
				calcPath.add(busPtr);
				busPrev = busPtr;
				busPtr = busPtr.next;
				walkPrev = busPrev.down;
				walkPtr =busPrev.down;
			} else {
				walkPrev = busPrev.down;
				walkPtr = busPrev.down;
				break;
			}
				
		}
		while(walkPtr != null){
			if(walkPtr.next == null){
				calcPath.add(walkPtr);
				break;
			}
			else if(walkPtr.location == destination){
				calcPath.add(walkPtr);
				break;
			}
			 else {
				calcPath.add(walkPtr);
				walkPrev = walkPtr;
				walkPtr = walkPtr.next;
			}
			walkPrev = walkPrev.next;
		}
		return calcPath;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @return
	 */
	public static TNode duplicate(TNode trainZero) {
		if(trainZero == null){
			return null;
		}
		TNode cloneTr = clone(trainZero);
		TNode cloneBus = clone(trainZero.down);
		TNode cloneWlk = clone(trainZero.down.down);
		TNode busWlk = null;
		TNode bus = cloneBus;
		TNode wlk = cloneWlk;
		while(bus != null){
			busWlk = cloneDownConnect(bus, wlk);
			break;
		}
		TNode newTrainHead = null;
		TNode tr = cloneTr;
		TNode bs = cloneBus;
		while(tr != null){
			newTrainHead = cloneDownConnect(tr, bs);
			break;
		}
		return newTrainHead;
	}
		
	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public static void addScooter(TNode trainZero, int[] scooterStops) {
		TNode busPtr = trainZero.down;
		TNode busPrev = busPtr;
		TNode walkingHead = trainZero.down.down;
		TNode walkingPtr = trainZero.down.down;
		TNode scooterHead = new TNode();
		scooterHead.down = walkingPtr;
		busPrev.down = scooterHead;
		TNode scooterPrev = scooterHead;
		TNode scooterPtr = scooterHead;
		for(int i = 0; i<scooterStops.length; i++){
			if(i == 0){
			int sStop = scooterStops[i];
			scooterPtr = new TNode(sStop, null, null);
			scooterPrev.next = scooterPtr;
			scooterPtr.down = downConnection(sStop, walkingHead);
			} else {
				int sStop = scooterStops[i];
				scooterPrev = scooterPtr;
				scooterPtr = new TNode(sStop, null, null);
				scooterPrev.next = scooterPtr;
				scooterPtr.down = downConnection(sStop, walkingHead);

			}
		}
		int n = 0;
		while (busPtr != null){
			int sStop = scooterStops[n];
			if(busPtr.location == sStop){
			busPtr.down = downConnection(sStop, scooterHead);
			busPrev = busPtr;
			busPtr = busPtr.next;
			n++;
			} else{
				busPrev = busPtr;
				busPtr = busPtr.next;

			}
		}
	}


// private methods

// takes two linked lists and itirates through both
// connects the top node with the bottom node that has the same value
// returns the first node of the top layer
private static TNode cloneDownConnect(TNode up, TNode down){
	TNode top = up;
	TNode bottom = down;
	TNode head = top;
	while(top != null){
		if(top.location == bottom.location){
			top.down = bottom;
			top = top.next;
		} else {
			bottom = bottom.next;
		}
	}
	return head;
}

// takes an int value location from middle layer or top layer and the head of the lower layer list 
// and returns the node that has the same location in the next lower layer
private static TNode downConnection(int up, TNode down){
	 TNode x = down;
	for(TNode ptr = down; ptr != null; ptr = ptr.next){
		if(ptr.location == up){
			x = ptr;
		}
	}
	return x;
}
private static TNode clone(TNode zeroNode){
	if(zeroNode == null){
		return null;
	}

	// creates new nodes and stores the values from the original nodes into the new nodes
	// inserts each new node as next of each of the original nodes
	// the list will have both the original and the new nodes at the end
	TNode ptr = zeroNode;
	while(ptr != null){
			TNode clone = new TNode(ptr.location);
			clone.next = ptr.next;
			ptr.next = clone;
			ptr = clone.next;
	}

	// break lists into two as original and clone list 
	// returns the head of the clone list
	ptr = zeroNode;	
	TNode newHead = zeroNode.next;	
	while (ptr != null) {
		TNode temp = ptr.next;		
		ptr.next = temp.next;	
		if (temp.next != null){
			temp.next = temp.next.next;
		}
		ptr = ptr.next;
	}
	return newHead;
	}
}
