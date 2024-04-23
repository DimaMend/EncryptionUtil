import java.util.List;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "config-properties")
@Getter
@Setter
public class ConfigProperties {
	private List<AesConfig> aesConfig;
	private List<AuthConfig> authConfig;

    @Getter
    @Setter
    public static class AesConfig{

    	private String portal;
    	private String aesKey;
    	private String aesIv;
    }

    @Getter
    @Setter
    public static class AuthConfig{

        private String portal;
        private String userName;
        private String password;
        private String appCode;
        private String clientId;
        private String clientSecret;
    }
}