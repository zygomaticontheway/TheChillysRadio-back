package theChillys.chillys_radio.data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class DNSLookup {
    public List<String> performDNSLookup() {

        try {
            // Доменное имя для разрешения
            String domainName = "all.api.radio-browser.info";

            // Получение информации о домене
            InetAddress[] addresses = InetAddress.getAllByName(domainName);
            List<String> hosts = new ArrayList<>();


            for (InetAddress address : addresses) {
                hosts.add(address.getHostAddress());
            }

            hosts.get(new Random().nextInt(hosts.size()));

            return hosts;

        } catch (UnknownHostException e) {

            return new ArrayList<>();
        }
    }

    public String getRandomHost (){

        List<String> hosts = performDNSLookup();
        System.out.println(hosts.get(new Random().nextInt(hosts.size())));

        return hosts.get(new Random().nextInt(hosts.size()));
    }
}
