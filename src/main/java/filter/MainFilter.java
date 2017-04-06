package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.*;


/**
 * Servlet Filter implementation class MainFilter
 */
@WebFilter(description = "the main filter", urlPatterns = { "/MainFilter" })
public class MainFilter implements Filter {
	protected String encoding = null;
    public static Logger logger = Logger.getLogger("MainFilter");
    /**
     * Default constructor. 
     */
    public MainFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		logger.info("=============================Start MainFilter::doFilter========================");		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		if (req.getMethod().equals("POST")) {
			request.setCharacterEncoding(this.encoding);
			response.setCharacterEncoding(this.encoding);
		} else {
			request = new FRequest(req);
		}
		if (req.getParameter("a") != null) {
			logger.info("==== a is " + req.getParameter("a") + "====");
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		logger.info("Start MainFilter::init");
		this.encoding = fConfig.getInitParameter("encoding");
	}
	
	class FRequest extends HttpServletRequestWrapper {

		public FRequest(HttpServletRequest request) {
			super(request);
		}

		public String toChi(String input) {
			String s = null;
			try {
				if (input != null) {
					byte[] bytes = input.getBytes("ISO-8859-1");
					s = new String(bytes, encoding);
				}
			} catch (Exception ex) {
			}
			return s;
		}

		private HttpServletRequest getHttpServletRequest() {
			return (HttpServletRequest) super.getRequest();
		}

		public String getParameter(String name) {
			return this.toChi(this.getHttpServletRequest().getParameter(name));
		}

		public String[] getParameterValues(String name) {
			String values[] = this.getHttpServletRequest().getParameterValues(
					name);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					values[i] = this.toChi(values[i]);
				}
			}
			return values;
		}
	}

}
