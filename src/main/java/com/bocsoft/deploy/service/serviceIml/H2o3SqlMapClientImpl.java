package main.java.com.bocsoft.deploy.service.serviceIml;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.builder.xml.SqlMapConfigParser;
import com.ibatis.sqlmap.engine.builder.xml.XmlParserState;
import com.ibatis.sqlmap.engine.config.SqlMapConfiguration;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * sqlMapClientImpl�Ĵ����ࡣ
 * 
 * <pre>
 * 		�����������sql��������ڣ����Ҵ����SqlMapExecutorDelegate���sqlִ�����Ĵ����ࡣ
 * 		�������������spring�е���ڡ�
 * 		
 * 		���Դ�������࣬���Լ���delegate���ý�ȥ�����ҷ����Լ���delegate��SqlMapConfiguration��
 * 		��֤���¼���sqlmap��ʱ�򣬲��������Լ���delegate�������Ͳ��ᴥ��ԭ��delegate�е��ظ��жϡ�
 * </pre>
 * 
 * @author yuezhen
 * 
 */
public class H2o3SqlMapClientImpl extends SqlMapClientImpl {

	/**
	 * Delegate for SQL execution
	 */
	public H2o3SqlMapExecutorDelegate h2o3Delegate;

	/**
	 * sqlmap��·��
	 */
	private Resource[] configLocations;

	/**
	 * configת����
	 */
	private SqlMapConfigParser configParser;

	/**
	 * sqlmapclient���õ�properties��spring����
	 */
	private Properties properties;

	/**
	 * ���췽����
	 * 
	 * @param client
	 * @param configLocations
	 * @param configParser
	 * @param properties
	 */
	public H2o3SqlMapClientImpl(SqlMapClient client,
                                Resource[] configLocations, SqlMapConfigParser configParser,
                                Properties properties) {
		super(new H2o3SqlMapExecutorDelegate(((ExtendedSqlMapClient) client)
				.getDelegate()));
		this.h2o3Delegate = (H2o3SqlMapExecutorDelegate) this.delegate;
		this.configLocations = configLocations;
		this.configParser = configParser;
		this.properties = properties;
		relfectDelegate();
	}

	/**
	 * ����ˢ�¡�
	 * 
	 * @throws IOException
	 */
	public void fresh() throws IOException {

		// ����configParser�����¼���
		for (Resource configLocation : configLocations) {
			InputStream is = configLocation.getInputStream();
			try {
				configParser.parse(is, properties);
			} catch (RuntimeException ex) {
				throw new NestedIOException("Failed to parse config resource: "
						+ configLocation, ex.getCause());
			}
		}
	}

	/**
	 * ���佫�Լ���delegate�����䵽SqlMapConfiguration�С�
	 */
	public void relfectDelegate() {
		try {
			Field stateField = this.configParser.getClass().getDeclaredField(
					"state");
			stateField.setAccessible(true);
			XmlParserState state = (XmlParserState) stateField
					.get(this.configParser);
			Field configFiled = state.getClass().getDeclaredField("config");
			configFiled.setAccessible(true);
			SqlMapConfiguration configField = (SqlMapConfiguration) configFiled
					.get(state);
			Field clientField = configField.getClass().getDeclaredField(
					"client");
			clientField.setAccessible(true);
			clientField.set(configField, this);
			Field delegateField = configField.getClass().getDeclaredField(
					"delegate");
			delegateField.setAccessible(true);
			delegateField.set(configField, this.delegate);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public H2o3SqlMapExecutorDelegate getMydelegate() {
		return h2o3Delegate;
	}
}
