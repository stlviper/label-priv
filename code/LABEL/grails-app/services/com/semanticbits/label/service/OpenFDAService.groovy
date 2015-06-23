package com.semanticbits.label.service

/**
 * openFDA API invocation service
 * @author gopal
 */
class OpenFDAService {
    
    def grailsApplication

    /**
     * Invoke openFDA API and return response
     * @param searchParams search parameters in a Map
     * @return return the server response from the API invocation, return empty string if term was not found
     * @throws LabelServiceException if there are any errors during the invocation
     */
    @SuppressWarnings(['UnnecessaryElseStatement'])
    def invoke(Map<String, String> searchParams) throws LabelServiceException {
        try {
            if (!grailsApplication.config.openFDA.API.url) {
                throw new LabelServiceException('openFDA.API.url property not configured, unable to invoke openFDA API')
            }
            if (searchParams?.search) {
                StringBuilder sb = new StringBuilder(grailsApplication.config.openFDA.API.url).append('?')
                if (grailsApplication.config.openFDA.API.key) {
                    sb.append('api_key=').append(grailsApplication.config.openFDA.API.key)
                }
                searchParams.each { param, val ->
                    sb.append('&').append(param).append('=').append(val)
                }
                return new URL(sb.toString()).text
            } else {
                throw new LabelServiceException('search parameter must be specified for invoking the openFDA API')
            }
        } catch (FileNotFoundException fnfe) {
           return '' // term not found return ''
        } catch (all) {
            if (all in LabelServiceException) {
                throw all
            } 
            throw new LabelServiceException('Error invoking the openFDA API', all)
        }
    }
}
