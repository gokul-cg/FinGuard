package com.main.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.main.dto.PortfolioRequest;
import com.main.entity.Portfolio;
import com.main.exception.CustomException;
import com.main.repository.PortfolioRepository;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public Portfolio addPortfolio(PortfolioRequest request) {
        logger.info("Adding portfolio for user ID: {}", request.getUserId());
        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(request.getUserId());
        portfolio.setAssetType(request.getAssetType());
        portfolio.setQuantity(request.getQuantity());
        portfolio.setPurchasePrice(request.getPurchasePrice());
        portfolio.setCurrentPrice(request.getCurrentPrice());
        
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        logger.info("Portfolio added successfully with ID: {}", savedPortfolio.getUserId());
        return savedPortfolio;
    }

    @Override
    public Portfolio viewPortfolio(int portfolioId) {
        logger.info("Viewing portfolio with ID: {}", portfolioId);
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    logger.error("Portfolio not found with ID: {}", portfolioId);
                    return new CustomException("Portfolio not found with ID: " + portfolioId);
                });
    }

    @Override
    public Portfolio updatePortfolio(int portfolioId, PortfolioRequest portfolioRequest) {
        logger.info("Updating portfolio with ID: {}", portfolioId);
        
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    logger.error("Portfolio not found with ID: {}", portfolioId);
                    return new CustomException("Portfolio not found with ID: " + portfolioId);
                });

        if (portfolioRequest.getAssetType() == null) {
            logger.error("Portfolio Asset Type cannot be null for portfolio ID: {}", portfolioId);
            throw new CustomException("Portfolio Asset Type cannot be null.");
        }

        portfolio.setAssetType(portfolioRequest.getAssetType());
        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        logger.info("Portfolio updated successfully with ID: {}", updatedPortfolio.getUserId());
        return updatedPortfolio;
    }

    @Override
    public void deletePortfolio(int portfolioId) {
        logger.info("Deleting portfolio with ID: {}", portfolioId);
        
        if (!portfolioRepository.existsById(portfolioId)) {
            logger.error("Portfolio not found with ID: {}", portfolioId);
            throw new CustomException("Portfolio not found with ID: " + portfolioId);
        }

        portfolioRepository.deleteById(portfolioId);
        logger.info("Portfolio deleted successfully with ID: {}", portfolioId);
    }
}