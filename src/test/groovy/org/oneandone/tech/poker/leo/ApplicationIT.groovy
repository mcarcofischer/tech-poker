package org.oneandone.tech.poker.leo

import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest

import geb.spock.GebReportingSpec

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationIT extends GebReportingSpec {

    @LocalServerPort
    Integer port

    def 'index loads correctly'() {
        when:
        go "http://localhost:$port"

        then:
        title == 'Tech Poker'
    }
}
