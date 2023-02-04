package br.com.contas.service.impl;

import br.com.contas.domain.ContaPagamento;
import br.com.contas.repository.ContaPagamentoRepository;
import br.com.contas.service.ContaPagamentoService;
import br.com.contas.service.dto.ContaPagamentoDTO;
import br.com.contas.service.mapper.ContaPagamentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContaPagamento}.
 */
@Service
@Transactional
public class ContaPagamentoServiceImpl implements ContaPagamentoService {

    private final Logger log = LoggerFactory.getLogger(ContaPagamentoServiceImpl.class);

    private final ContaPagamentoRepository contaPagamentoRepository;

    private final ContaPagamentoMapper contaPagamentoMapper;

    public ContaPagamentoServiceImpl(ContaPagamentoRepository contaPagamentoRepository, ContaPagamentoMapper contaPagamentoMapper) {
        this.contaPagamentoRepository = contaPagamentoRepository;
        this.contaPagamentoMapper = contaPagamentoMapper;
    }

    @Override
    public ContaPagamentoDTO save(ContaPagamentoDTO contaPagamentoDTO) {
        log.debug("Request to save ContaPagamento : {}", contaPagamentoDTO);
        ContaPagamento contaPagamento = contaPagamentoMapper.toEntity(contaPagamentoDTO);
        contaPagamento = contaPagamentoRepository.save(contaPagamento);
        return contaPagamentoMapper.toDto(contaPagamento);
    }

    @Override
    public ContaPagamentoDTO update(ContaPagamentoDTO contaPagamentoDTO) {
        log.debug("Request to update ContaPagamento : {}", contaPagamentoDTO);
        ContaPagamento contaPagamento = contaPagamentoMapper.toEntity(contaPagamentoDTO);
        contaPagamento = contaPagamentoRepository.save(contaPagamento);
        return contaPagamentoMapper.toDto(contaPagamento);
    }

    @Override
    public Optional<ContaPagamentoDTO> partialUpdate(ContaPagamentoDTO contaPagamentoDTO) {
        log.debug("Request to partially update ContaPagamento : {}", contaPagamentoDTO);

        return contaPagamentoRepository
            .findById(contaPagamentoDTO.getId())
            .map(existingContaPagamento -> {
                contaPagamentoMapper.partialUpdate(existingContaPagamento, contaPagamentoDTO);

                return existingContaPagamento;
            })
            .map(contaPagamentoRepository::save)
            .map(contaPagamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContaPagamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContaPagamentos");
        return contaPagamentoRepository.findAll(pageable).map(contaPagamentoMapper::toDto);
    }

    public Page<ContaPagamentoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contaPagamentoRepository.findAllWithEagerRelationships(pageable).map(contaPagamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContaPagamentoDTO> findOne(Long id) {
        log.debug("Request to get ContaPagamento : {}", id);
        return contaPagamentoRepository.findOneWithEagerRelationships(id).map(contaPagamentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContaPagamento : {}", id);
        contaPagamentoRepository.deleteById(id);
    }
}
