package com.semanticbits.label.controller

import com.semanticbits.label.service.SearchService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * User: JanakiRam Gollapudi
 * Date: 6/22/15
 */
@TestFor(SearchController)
//@Mock(SearchService)
class SearchControllerSpec extends Specification {

    def setup() {

    }

    def 'test label home page'() {
        when:'I call index method'
        controller.index()

        then:'I should be able to see lable home page'
        view == '/search/index'
    }

    def 'test if home page redirect to search results page'(){
        when:'I try to search label'
        controller.params.term = 'motrin'
        controller.index()

        then:'I should be redirected to search results page'
        view == '/search/results'
    }

    def 'test search by label'() {
        given:'construct a json to return as response for search method'
        def labels = []
        def labelsMap = [:]
        labelsMap["id"] = 1
        labelsMap["title"] = 'sample title'
        labelsMap['description'] = 'test description'
        labels.add(labelsMap)
        Map<String, Object> results =    [
                totalCount: 10,
                labels:labels
        ]

        when:'I try to search label with valid value'
        controller.params.draw = 0
        controller.params.term = 'motrin'
        def mockSearchService =  mockFor(SearchService)
        mockSearchService.demand.search {obj1, obj2 -> results }
        controller.searchService = mockSearchService.createMock()
        controller.searchJSON()

        then:'I should get valid results'
        response.json.iTotalRecords == results.totalCount
        response.json.iTotalDisplayRecords == results.totalCount
        response.json.aaData[0].labelDetails.id ==  results.labels[0].id
        response.json.aaData[0].labelDetails.title ==  results.labels[0].title
        response.json.aaData[0].labelDetails.description ==  results.labels[0].description
    }

    def 'test search by label with empty string'() {
        when:'I try to search label with valid value'
        controller.params.draw = 0
        controller.params.term = ''
        controller.searchJSON()

        then:'I should get valid results'
        response.json.iTotalRecords == 0
        response.json.iTotalDisplayRecords == 0
        response.json.aaData == []
    }


}