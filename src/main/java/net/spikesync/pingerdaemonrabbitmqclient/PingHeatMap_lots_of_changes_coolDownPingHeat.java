package net.spikesync.pingerdaemonrabbitmqclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spikesync.pingerdaemonrabbitmqclient.PingEntry.PINGHEAT;
import net.spikesync.pingerdaemonrabbitmqclient.PingEntry.PINGRESULT;

public class PingHeatMap {

	private static final Logger logger = LoggerFactory.getLogger(PingHeatMap.class);

	private HashMap<SilverCloudNode, HashMap<SilverCloudNode, PingHeatData>> pingHeatMap;

	public PingHeatMap(SilverCloud sc) {

		int colCount = 0;
		int rowCount = 0;

		pingHeatMap = new HashMap<SilverCloudNode, HashMap<SilverCloudNode, PingHeatData>>();

		for (SilverCloudNode rowNode : sc.getScNodes()) {

			// Create a new row for the pingHeatMap: this is a new HashMap!!
			HashMap<SilverCloudNode, PingHeatData> colEntry = new HashMap<SilverCloudNode, PingHeatData>();
			// Put the new row in the pingHeatMap
			pingHeatMap.put(rowNode, colEntry);

			for (SilverCloudNode colNode : sc.getScNodes()) {

				// Put a new column entry into the current row of the pingHeatMap, i.e., an
				// entry in the row-HashMap.
				colEntry.put(new SilverCloudNode(colNode), new PingHeatData(
						PingEntry.PINGHEAT.UNKNOWN)); /*
														 * The default meaningless value of -1 is the default and means
														 * there is no real value for the ping heat.
														 */

				logger.debug("New column Entry: " + colNode.toString());
				logger.debug("Putting Node " + rowNode.getNodeName() + ", " + colNode.getNodeName() + " in PingHeatMap"
						+ " -- col, row: (" + colCount + ", " + rowCount + ") ");
				colCount++; // Increment the column number
			}
			// Add the fully filled column to the pingHeatmap
			pingHeatMap.put(new SilverCloudNode(rowNode), colEntry);
			logger.debug("Current row of NODE: " + rowNode.getNodeName() + " IN the pingHeatMap: "
					+ this.pingHeatMap.get(rowNode).toString());

			rowCount++; // Increment the column number
			colCount = 0; // Start a new column and set it to index 0
		}
	}

	// Change to private access when the class is working properly
	private HashMap<SilverCloudNode, HashMap<SilverCloudNode, PingHeatData>> getPingHeatmap() {
		return pingHeatMap;
	}

	public PingHeatData getPingHeat(SilverCloudNode rowNode, SilverCloudNode colNode) {
		// HashMap<SilverCloudNode, Integer> row = pingHeatMap.get(rowNode);
		// logger.debug("pingHeatMap contains node: " + rowNode.getNodeName() + ", yes?
		// " + pingHeatMap.containsKey(rowNode));
		// logger.debug("##################@@@@@@@@@@@@@@@@@@@@@@ Value of rownode: " +
		// row.toString());

		// Integer heat = pingHeatMap.get(rowNode).get(colNode);
		// logger.debug("pingHeat of nodes " + rowNode.getNodeName() + ", " +
		// rowNode.getNodeName() + ": " + heat);
		return pingHeatMap.get(rowNode).get(colNode);
	}

	public void coolDownPingHeat() {
		for (Entry<SilverCloudNode, HashMap<SilverCloudNode, PingHeatData>> rowNode : pingHeatMap.entrySet()) {
			for (Entry<SilverCloudNode, PingHeatData> colNode : rowNode.getValue().entrySet()) {
				/*
				 * PINGHEAT is now embedded in PingHeatData, so first construct a new instance
				 * of PingHeatData with the new value of PINGHEAT, and than put the PingHeatData
				 * instance into the column!
				 */
				logger.debug("pingHeat of pair after cool-down: (" + rowNode.getKey().getNodeName() + ", "
						+ colNode.getKey().getNodeName() + "): " + colNode.getValue().getPingHeat());

				long currentMillis = new Date().getTime();
				PingHeatData cellHeatData = colNode.getValue();

				if ((cellHeatData.getLastPingAttempt() == null) || (cellHeatData.getLastPingFailure() == null)
						|| (cellHeatData.getLastPingSuccess() == null));
				/* 				 DEBUG one of the ping TIMES is NOT accurate!!

					 If one of the last attempts is null, there is no comparison possible and no
					 pingHeat value should be assigned;
				 	 it remains UNKNONWN!

					if (cellHeatData.getLastPingAttempt() == null) {
						logger.debug("Last ping ATTEMPT of node (" + rowNode.getKey().getNodeName() + ", "
								+ colNode.getKey().getNodeName() + ") is NULL!!! ");
					}
					if (cellHeatData.getLastPingSuccess() == null) {
						logger.debug("Last ping SUCCESS of node (" + rowNode.getKey().getNodeName() + ", "
								+ colNode.getKey().getNodeName() + ") is NULL!!! ");
					}
					if (cellHeatData.getLastPingFailure() == null) {
						logger.debug("Last Ping FAILURE of node (" + rowNode.getKey().getNodeName() + ", "
								+ colNode.getKey().getNodeName() + ") is NULL!!! ");
					}

				}*/
				else {
					long lastPingMillis = cellHeatData.getLastPingAttempt().getTime();
					long successfulPingMillis = cellHeatData.getLastPingSuccess().getTime();
					long failedPingMillis = cellHeatData.getLastPingFailure().getTime();
					long lastOrFailedMillis = failedPingMillis > lastPingMillis ? failedPingMillis : lastPingMillis;
//					if(colNode.getKey().getNodeName().equals("CAPTUW")) 
//						logger.debug("Data of node: CAPTUW \n" + 
//							"last ping attempt: " +	cellHeatData.getLastPingAttempt().getTime() +
//							"\nlast successful ping: " + cellHeatData.getLastPingSuccess().getTime() +
//							"\nlast failed ping: " + cellHeatData.getLastPingFailure());						
					if ((currentMillis - successfulPingMillis > 2000))
						colNode.setValue(new PingHeatData(PingEntry.getColderHeat(cellHeatData.getPingHeat())));
				}
			}
		}

	}

	public void setPingHeat(SilverCloudNode rowNode, SilverCloudNode colNode, PingHeatData heat) {
		this.pingHeatMap.get(rowNode).put(colNode, heat);
	}

	public void setPingHeat(ArrayList<PingEntry> pingEntries) {
		for (PingEntry pingEntry : pingEntries) {
			SilverCloudNode rowNode = pingEntry.getPingOrig();
			SilverCloudNode colNode = pingEntry.getPingDest();
			PingHeatData currentPingHeat = getPingHeat(rowNode, colNode); // How to get the next warmer value of
																			// PINGHEAT on pinguccess??
			PingHeatData nextPingHeat = new PingHeatData(PINGHEAT.UNKNOWN); // Set the default value of the PINGHEAT to
																			// UNKNOWN
			if (pingEntry.getLastPingResult().equals(PingEntry.PINGRESULT.PINGSUCCESS)) {
				nextPingHeat = new PingHeatData(PingEntry.getWarmerHeat(currentPingHeat.getPingHeat()));
				Date now = new Date();
				nextPingHeat = new PingHeatData(PingEntry.getColderHeat(currentPingHeat.getPingHeat()));
				nextPingHeat.setLastPingFailure(now);
				nextPingHeat.setLastPingAttempt(now);
			} else if (pingEntry.getLastPingResult().equals(PINGRESULT.PINGFAILURE)) {
				Date now = new Date();
				nextPingHeat = new PingHeatData(PingEntry.getColderHeat(currentPingHeat.getPingHeat()));
				nextPingHeat.setLastPingFailure(now);
				nextPingHeat.setLastPingAttempt(now);
			}
			setPingHeat(rowNode, colNode, nextPingHeat);
			logger.info("Set pingheat of (rowNode, colNode): (" + rowNode.getNodeName() + ", " + colNode.getNodeName() + 
					") to:" + nextPingHeat.getPingHeat());

		}
	}

}