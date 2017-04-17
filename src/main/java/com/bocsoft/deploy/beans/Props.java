package main.java.com.bocsoft.deploy.beans;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Jean on 2017/4/14.
 */
public class Props {
    //config.properties的类路径
    private final String CONFIGCLASSPATH = "main/resources/config.properties";
    private Properties props;

    public Props() {
        try {
            setProps(PropertiesLoaderUtils.loadProperties(new ClassPathResource(CONFIGCLASSPATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProps() {
        return this.props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
