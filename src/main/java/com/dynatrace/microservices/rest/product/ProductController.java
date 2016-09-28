package com.dynatrace.microservices.rest.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.ServiceProperties;
import com.dynatrace.microservices.remoting.product.ProductService;
import com.dynatrace.microservices.rest.common.CommonController;

@RestController("products")
@ConditionalOnExpression("!'${micro.service}'.equals('registry') && !'${micro.service}'.equals('gateway') && !'${micro.service}'.equals('controller')")
public class ProductController implements ProductService {
	
	private static final Log LOGGER = LogFactory.getLog(CommonController.class.getName());
	
	@Autowired
	ServiceProperties props;
	
	@Override
	public ProductReferences getProducts() {
		try {
			ProductReferences products = new ProductReferences();
			DataSource dataSource = ServiceApplication.getDataSource();
			try (
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM product")
			) {
				while (rs.next()) {
					products.add(new ProductReference(rs.getInt(1)));
				}
			}
			return products;
		} catch (Throwable t) {
			return new ProductReferences();
		}
	}

	@Override
	public Product getProduct(@PathVariable String productId) {
		try {
			DataSource dataSource = ServiceApplication.getDataSource();
			try (
				Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM product WHERE id = ?");
			) {
				statement.setInt(1, Integer.parseInt(productId));
				try (ResultSet rs = statement.executeQuery()) {
					if (!rs.next()) {
						return Product.NOT_FOUND;
					}
					Product product = new Product();
					product.setId(rs.getInt(1));
					product.setName(rs.getString(2));
					product.setPrice(rs.getInt(3));
					return product;
				}
			}
		} catch (Throwable t) {
			LOGGER.warn("...failed", t);
			return Product.ERROR;
		}
	}

	@Override
	public Price getPrice(@PathVariable String productId) {
		try {
			DataSource dataSource = ServiceApplication.getDataSource();
			try (
				Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT price FROM product WHERE id = ?");
			) {
				statement.setInt(1, Integer.parseInt(productId));
				try (ResultSet rs = statement.executeQuery()) {
					if (!rs.next()) {
						return Price.NOT_FOUND;
					}
					return new Price(rs.getInt(1));
				}
			}
		} catch (Throwable t) {
			LOGGER.warn("...failed", t);
			return Price.ERROR;
		}
	}

}