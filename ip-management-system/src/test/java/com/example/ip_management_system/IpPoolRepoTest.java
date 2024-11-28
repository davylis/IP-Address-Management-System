package com.example.ip_management_system;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.repositories.IpPoolRepository;

@DataJpaTest
public class IpPoolRepoTest {

    @Autowired
    private IpPoolRepository ipPoolRepository;

    @Test
    public void findByNameShouldReturnIpPool() {
        // Assuming there's an IpPool with the name "Test Pool" already in the database
        List<IpPool> ipPools = ipPoolRepository.findByName("Test Pool");
        assertThat(ipPools).hasSize(1);
        assertThat(ipPools.get(0).getName()).isEqualTo("Test Pool");
    }

    @Test
    public void findByStartIpShouldReturnIpPool() {
        // Assuming there's an IpPool with start IP "192.168.0.1" already in the database
        List<IpPool> ipPools = ipPoolRepository.findByStartIp("192.168.0.1");
        assertThat(ipPools).isNotEmpty();
        assertThat(ipPools.get(0).getStartIp()).isEqualTo("192.168.0.1");
    }

    @Test
    public void findByStartIpAndEndIpShouldReturnIpPool() {
        // Assuming there's an IpPool with start IP "192.168.0.1" and end IP "192.168.0.255"
        List<IpPool> ipPools = ipPoolRepository.findByStartIpAndEndIp("192.168.0.1", "192.168.0.255");
        assertThat(ipPools).isNotEmpty();
        assertThat(ipPools.get(0).getStartIp()).isEqualTo("192.168.0.1");
        assertThat(ipPools.get(0).getEndIp()).isEqualTo("192.168.0.255");
    }

    @Test
    public void testCreateNewIpPool() {
        IpPool ipPool = new IpPool();
        ipPool.setName("New Pool");
        ipPool.setStartIp("10.0.0.1");
        ipPool.setEndIp("10.0.0.255");
        ipPool.setDescription("Test IP Pool");

        ipPoolRepository.save(ipPool);

        assertThat(ipPool.getId()).isNotNull();
    }

    @Test
    public void testDeleteIpPool() {
        // Assuming there's an IpPool with the name "Test Pool" already in the database
        List<IpPool> ipPools = ipPoolRepository.findByName("Test Pool");
        Long ipPoolId = ipPools.get(0).getId();

        ipPoolRepository.deleteById(ipPoolId);

        List<IpPool> deletedIpPools = ipPoolRepository.findByName("Test Pool");
        assertThat(deletedIpPools).isEmpty();
    }
}
