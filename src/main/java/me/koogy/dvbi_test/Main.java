package me.koogy.dvbi_test;

import java.util.List;
import java.util.Map;
import nl.digitalekabeltelevisie.data.mpeg.PesPacketData;
import nl.digitalekabeltelevisie.data.mpeg.TSPacket;
import nl.digitalekabeltelevisie.data.mpeg.TransportStream;
import nl.digitalekabeltelevisie.data.mpeg.pes.GeneralPidHandler;
import nl.digitalekabeltelevisie.data.mpeg.pes.video264.Video14496Handler;
import nl.digitalekabeltelevisie.main.DVBinspector;

public class Main {

    public static void main(String[] args) throws Exception {
        String filename = "src/test/resources/example.ts";
        TransportStream ts = new TransportStream(filename);
        ts.parseStream();
        String[] myArgs = new String[]{filename, "481"};
        Map<Integer, GeneralPidHandler> pidHandlerMap = DVBinspector.determinePidHandlers(myArgs, ts);
        ts.parsePidStreams(pidHandlerMap);

        // PID 481 is the video
        GeneralPidHandler pidHandler = pidHandlerMap.get(481);
        Video14496Handler videoHandler = (Video14496Handler)pidHandler;
        List<PesPacketData> dataPackets = videoHandler.getPesPackets();
        // this should be 96, but is 95
        System.out.println("DataPackets size: " + dataPackets.size());
        // last one in list is number 94
        PesPacketData packet94 = dataPackets.get(94);
        System.out.println("PesPacketData[94] starts at: " + packet94.getStartPacketNo());
        
        // but ts packet [1701] is also a start packet for PID 481
        TSPacket tsPacket = ts.getTSPacket(1701);
        System.out.println("TsPacket[1701]"
                + " PID: " + tsPacket.getPID() + ","
                + " Start: " + tsPacket.isPayloadUnitStartIndicator()
        );
    }
}
