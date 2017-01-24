package org.oneandone.tech.poker.leo

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

import javax.inject.Inject

import org.oneandone.tech.poker.leo.services.GameService
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

import spock.lang.Specification

@AutoConfigureMockMvc
@SpringBootTest
class PokerControllerTest extends Specification {

    @Inject MockMvc mockMvc
    @Inject GameService gameService

    def "renders index"() {
        expect:
        mockMvc.perform(get('/')).andExpect(view().name('index'))
    }

    def "starts new session index"() {
        expect:
        mockMvc.perform(post('/new')).andExpect(redirectedUrlPattern('/game/*'))
    }


    def "qr url is correctly inserted"() {
        given:
        def gameId = gameService.createNewGame()
        expect:
        mockMvc.perform(get("/game/$gameId"))
                .andExpect(view().name('poker'))
                .andExpect(content().string(containsString("data-qr-url=\"http://localhost/game/$gameId\"")))
    }

    def "join asks for a name"() {
        given:
        def gameId = gameService.createNewGame()

        expect:
        mockMvc.perform(get("/join/$gameId")).andExpect(view().name('join'))
    }

    def "join accepts a name"() {
        given:
        def gameId = gameService.createNewGame()

        expect:
        mockMvc.perform(post("/join/$gameId").param('playerName', 'John Doe')).andExpect(redirectedUrlPattern('/vote/*/*'))
    }
}