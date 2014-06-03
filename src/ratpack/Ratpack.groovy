import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import hello.Person
import javax.sql.DataSource
import groovy.sql.Sql

ratpack {
  handlers {
    // test accessing Spring beans (the dataSource bean created by Spring Boot)
    get ('database') { DataSource dataSource ->
        def sql
        try { 
            sql = new Sql(dataSource)
            def result=[:] + sql.firstRow("select * from Person")
            render "first row in Person: $result"
        } finally {
            sql?.close()
        }
    }
    // test Gorm access
    get {
    	def p = Person.findByFirstName("Homer")
        if( !p ) {
            Person.withTransaction {
                p = new Person(firstName:"Homer", lastName:"Simpson")
                p.save()                
            }
        }
        render "Hello ${p.firstName}!"
    }
        
    assets "public"
  }
}
