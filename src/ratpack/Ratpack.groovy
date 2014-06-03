import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import hello.Person

ratpack {
  handlers {
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
