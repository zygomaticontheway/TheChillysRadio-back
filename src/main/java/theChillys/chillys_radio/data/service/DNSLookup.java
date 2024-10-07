package theChillys.chillys_radio.data.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import theChillys.chillys_radio.data.dto.DataResponseDto;
import theChillys.chillys_radio.data.dto.HostsResponseDto;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DNSLookup {

    ObjectMapper objectMapper = new ObjectMapper();

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

    private List<String> getHosts() {
        RestTemplate restTemplate = new RestTemplate();
        String URL = "http://all.api.radio-browser.info/json/servers";

        ResponseEntity<HostsResponseDto[]> response = restTemplate.getForEntity(URL, HostsResponseDto[].class);
        HostsResponseDto[] hostsResponse = response.getBody();

        List<String> hosts = Arrays.stream(hostsResponse)
                .map(HostsResponseDto::getName)
                .collect(Collectors.toList());
        System.out.println(hosts);

        return hosts;
    }

    public String getRandomHost (){

        List<String> hosts = getHosts();
        System.out.println(hosts.get(new Random().nextInt(hosts.size())));

        return hosts.get(new Random().nextInt(hosts.size()));
    }
}
