import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import hello.Person
import javax.sql.DataSource
import groovy.sql.Sql
import ratpack.spring.Spring

ratpack {
  handlers {
    register Spring.run(hello.Application)
    // test accessing Spring beans (the dataSource bean created by Spring Boot)
    get ('database') { DataSource dataSource ->
        blocking {
            def sql
            try { 
                sql = new Sql(dataSource)
                [:] + (sql.firstRow("select * from Person")?:[:])
            } finally {
                sql?.close()
            }
        } then { result ->
            render "first row in Person: $result"
        }
    }
    // test Gorm access
    get { 
        blocking {
        	def p = Person.findByFirstName("Homer")
            if( !p ) {
                Person.withTransaction {
                    p = new Person(firstName:"Homer", lastName:"Simpson")
                    p.save()                
                }
            }
            p
        } then { result ->
            render "Hello ${result.firstName}!"
        }
    }
        
    assets "public"
  }
}
