package odunlamizo.taskflow.auth.generator.id;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import odunlamizo.taskflow.auth.util.Strings;

public class StringGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
       return Strings.random(16);
    }
    
}
