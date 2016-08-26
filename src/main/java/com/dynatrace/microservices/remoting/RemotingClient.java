package com.dynatrace.microservices.remoting;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public interface RemotingClient {

	@SuppressWarnings("unchecked")
	public static <T, K extends T> K create(Class<T> c, ServiceInstance serviceInstance) {
		return (K) Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[] { c }, new RemotingHandler(serviceInstance, c));
	}
	
	public static class RemotingHandler implements InvocationHandler {
		
		private static final RestTemplate REST = new RestTemplate();
		private final ServiceInstance serviceInstance;
		private final Class<?> serviceInterface;
		
		public RemotingHandler(ServiceInstance serviceInstance, Class<?> serviceInterface) {
			this.serviceInstance = serviceInstance;
			this.serviceInterface = serviceInterface;
		}
		
		private static HttpMethod convert(RequestMethod methods[], HttpMethod def) {
			if (methods == null) {
				return def;
			}
			if (methods.length == 0) {
				return def;
			}
			return convert(methods[0], def);
		}
		
		private static HttpMethod convert(RequestMethod method, HttpMethod def) {
			if (method == null) {
				return def;
			}
			switch (method) {
			case DELETE:
				return HttpMethod.DELETE;
			case GET:
				return HttpMethod.GET;
			case HEAD:
				return HttpMethod.HEAD;
			case OPTIONS:
				return HttpMethod.OPTIONS;
			case PATCH:
				return HttpMethod.PATCH;
			case POST:
				return HttpMethod.POST;
			case PUT:
				return HttpMethod.PUT;
			case TRACE:
				return HttpMethod.TRACE;
			default:
				return def;
			}
		}
		
		private static HttpMethod httpMethod(Annotation annotation, HttpMethod def) {
			if (annotation == null) {
				return def;
			}
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (PostMapping.class.equals(annotationType)) {
				return HttpMethod.POST;
			} else if (RequestMapping.class.equals(annotationType)) {
				RequestMapping mapping = (RequestMapping) annotation;
				return convert(mapping.method(), def);
			} else if (PutMapping.class.equals(annotationType)) {
				return HttpMethod.PUT;
			} else if (PatchMapping.class.equals(annotationType)) {
				return HttpMethod.PATCH;
			} else if (DeleteMapping.class.equals(annotationType)) {
				return HttpMethod.DELETE;
			}
			return def;
		}
		
		private static String contentType(Annotation annotation, String def) {
			if (annotation == null) {
				return def;
			}
			String[] consumes = null;
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (PostMapping.class.equals(annotationType)) {
				PostMapping mapping = (PostMapping) annotation;
				consumes = mapping.consumes();
			} else if (RequestMapping.class.equals(annotationType)) {
				RequestMapping mapping = (RequestMapping) annotation;
				consumes = mapping.consumes();
			} else if (PutMapping.class.equals(annotationType)) {
				PutMapping mapping = (PutMapping) annotation;
				consumes = mapping.consumes();
			} else if (PatchMapping.class.equals(annotationType)) {
				PatchMapping mapping = (PatchMapping) annotation;
				consumes = mapping.consumes();
			} else if (DeleteMapping.class.equals(annotationType)) {
				DeleteMapping mapping = (DeleteMapping) annotation;
				consumes = mapping.consumes();
			}
			if ((consumes != null) && (consumes.length > 0)) {
				return consumes[0];
			}
			return def;
		}
		
		private static String path(Annotation annotation, String def) {
			if (annotation == null) {
				return def;
			}
			String[] paths = null;
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (PostMapping.class.equals(annotationType)) {
				PostMapping mapping = (PostMapping) annotation;
				paths = mapping.path();
			} else if (RequestMapping.class.equals(annotationType)) {
				RequestMapping mapping = (RequestMapping) annotation;
				paths = mapping.path();
			} else if (PutMapping.class.equals(annotationType)) {
				PutMapping mapping = (PutMapping) annotation;
				paths = mapping.path();
			} else if (GetMapping.class.equals(annotationType)) {
				GetMapping mapping = (GetMapping) annotation;
				paths = mapping.path();
			} else if (PatchMapping.class.equals(annotationType)) {
				PatchMapping mapping = (PatchMapping) annotation;
				paths = mapping.path();
			} else if (DeleteMapping.class.equals(annotationType)) {
				DeleteMapping mapping = (DeleteMapping) annotation;
				paths = mapping.path();
			}
			if ((paths != null) && (paths.length > 0)) {
				return paths[0];
			}
			return def;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String url = serviceInstance.createURL(method.getName(), false).toString();
			HttpMethod httpMethod = HttpMethod.GET;
			String path = "";
			String contentType = null;
			System.out.println("-------------- " + "proxy: " + proxy.getClass().getName());
			System.out.println("-------------- " + "method: " + method.getName());
			Annotation[] annotations = method.getAnnotations();
			System.out.println("-------------- " + "annotations");
			if (annotations != null) {
				for (Annotation annotation : annotations) {
					contentType = contentType(annotation, contentType);
					httpMethod = httpMethod(annotation, httpMethod);
					path = path(annotation, path);
					System.out.println("-------------- " + "  " + annotation.annotationType().getSimpleName());
				}
			}
			
			System.out.println("-------------- " + "parameters");
			
			Parameter[] parameters = this.serviceInterface.getMethod(method.getName(), method.getParameterTypes()).getParameters();
			if (parameters != null) {
				for (Parameter parameter : parameters) {
					System.out.println("-------------- " + "  " + parameter.getName());
				}
			}

			
			System.out.println("-------------- " + "URL: " + url);
			
			return null;
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<?> entity = new HttpEntity<Object>(args[0], headers);
//			
//			//REST.getForEntity()
//			
//			ResponseEntity<?> response = REST.getForEntity(url, method.getReturnType());
//			return response.getBody();
		}
		
	}
}
