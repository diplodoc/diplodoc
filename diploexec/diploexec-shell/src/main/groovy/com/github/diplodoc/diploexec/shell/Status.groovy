package com.github.diplodoc.diploexec.shell

import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
RestTemplate client = new RestTemplate()
def status = client.getForObject('http://localhost:8080/diploexec/api/status', List.class)

println "RUNNING"
println "id".padRight(40) + "name".padRight(30) + "status".padRight(10) + "start".padRight(30) + "end".padRight(30) + "description".padRight(80)
status.findAll { it.status == "RUNNING" }.each {
    println "${it.id}".padRight(40) + "${it.name}".padRight(30) + "${it.status}".padRight(10) + "${it.startTime}".padRight(30) + "${it.endTime}".padRight(30) + "${it.description}".padRight(80)
}
println()

println "WAITING"
println "id".padRight(40) + "name".padRight(30) + "status".padRight(10) + "start".padRight(30) + "end".padRight(30) + "description".padRight(80)
status.findAll { it.status == "WAITING" }.each {
    println "${it.id}".padRight(40) + "${it.name}".padRight(30) + "${it.status}".padRight(10) + "${it.startTime}".padRight(30) + "${it.endTime}".padRight(30) + "${it.description}".padRight(80)
}
println()

println "FINISHED"
println "id".padRight(40) + "name".padRight(30) + "status".padRight(10) + "start".padRight(30) + "end".padRight(30) + "description".padRight(80)
status.findAll { it.status == "FINISHED" }.each {
    println "${it.id}".padRight(40) + "${it.name}".padRight(30) + "${it.status}".padRight(10) + "${it.startTime}".padRight(30) + "${it.endTime}".padRight(30) + "${it.description}".padRight(80)
}
println()