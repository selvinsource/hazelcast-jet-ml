package org.selvinsource.hazelcast_jet_ml;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.stream.IStreamMap;

/**
 * Hello world!
 *
 */
public class JetMLDemo 
{
    public static void main( String[] args )
    {
        JetInstance jet = Jet.newJetInstance();
        // Create an additional instance; it will automatically
        // discover the first one and form a cluster
        Jet.newJetInstance();
 
        IStreamMap<String, Integer> map = jet.getMap("latitudes");
        map.put("London", 51);
        map.put("Paris", 48);
        map.put("NYC", 40);
        map.put("Sydney", -34);
        map.put("Sao Paulo", -23);
        map.put("Jakarta", -6);
        map.entrySet().stream().filter(e -> e.getValue() < 0).forEach(System.out::println);
        
        Jet.shutdownAll();    	
    }
}
