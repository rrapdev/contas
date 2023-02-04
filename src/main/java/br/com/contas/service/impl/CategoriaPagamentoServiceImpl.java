package br.com.contas.service.impl;

import br.com.contas.domain.CategoriaPagamento;
import br.com.contas.repository.CategoriaPagamentoRepository;
import br.com.contas.service.CategoriaPagamentoService;
import br.com.contas.service.dto.CategoriaPagamentoDTO;
import br.com.contas.service.mapper.CategoriaPagamentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoriaPagamento}.
 */
@Service
@Transactional
public class CategoriaPagamentoServiceImpl implements CategoriaPagamentoService {

    private final Logger log = LoggerFactory.getLogger(CategoriaPagamentoServiceImpl.class);

    private final CategoriaPagamentoRepository categoriaPagamentoRepository;

    private final CategoriaPagamentoMapper categoriaPagamentoMapper;

    public CategoriaPagamentoServiceImpl(
        CategoriaPagamentoRepository categoriaPagamentoRepository,
        CategoriaPagamentoMapper categoriaPagamentoMapper
    ) {
        this.categoriaPagamentoRepository = categoriaPagamentoRepository;
        this.categoriaPagamentoMapper = categoriaPagamentoMapper;
    }

    @Override
    public CategoriaPagamentoDTO save(CategoriaPagamentoDTO categoriaPagamentoDTO) {
        log.debug("Request to save CategoriaPagamento : {}", categoriaPagamentoDTO);
        CategoriaPagamento categoriaPagamento = categoriaPagamentoMapper.toEntity(categoriaPagamentoDTO);
        categoriaPagamento = categoriaPagamentoRepository.save(categoriaPagamento);
        return categoriaPagamentoMapper.toDto(categoriaPagamento);
    }

    @Override
    public CategoriaPagamentoDTO update(CategoriaPagamentoDTO categoriaPagamentoDTO) {
        log.debug("Request to update CategoriaPagamento : {}", categoriaPagamentoDTO);
        CategoriaPagamento categoriaPagamento = categoriaPagamentoMapper.toEntity(categoriaPagamentoDTO);
        categoriaPagamento = categoriaPagamentoRepository.save(categoriaPagamento);
        return categoriaPagamentoMapper.toDto(categoriaPagamento);
    }

    @Override
    public Optional<CategoriaPagamentoDTO> partialUpdate(CategoriaPagamentoDTO categoriaPagamentoDTO) {
        log.debug("Request to partially update CategoriaPagamento : {}", categoriaPagamentoDTO);

        return categoriaPagamentoRepository
            .findById(categoriaPagamentoDTO.getId())
            .map(existingCategoriaPagamento -> {
                categoriaPagamentoMapper.partialUpdate(existingCategoriaPagamento, categoriaPagamentoDTO);

                return existingCategoriaPagamento;
            })
            .map(categoriaPagamentoRepository::save)
            .map(categoriaPagamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaPagamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CategoriaPagamentos");
        return categoriaPagamentoRepository.findAll(pageable).map(categoriaPagamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaPagamentoDTO> findOne(Long id) {
        log.debug("Request to get CategoriaPagamento : {}", id);
        return categoriaPagamentoRepository.findById(id).map(categoriaPagamentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoriaPagamento : {}", id);
        categoriaPagamentoRepository.deleteById(id);
    }
}
