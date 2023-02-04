package br.com.contas.service.impl;

import br.com.contas.domain.ContaRecebimento;
import br.com.contas.repository.ContaRecebimentoRepository;
import br.com.contas.service.ContaRecebimentoService;
import br.com.contas.service.dto.ContaRecebimentoDTO;
import br.com.contas.service.mapper.ContaRecebimentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContaRecebimento}.
 */
@Service
@Transactional
public class ContaRecebimentoServiceImpl implements ContaRecebimentoService {

    private final Logger log = LoggerFactory.getLogger(ContaRecebimentoServiceImpl.class);

    private final ContaRecebimentoRepository contaRecebimentoRepository;

    private final ContaRecebimentoMapper contaRecebimentoMapper;

    public ContaRecebimentoServiceImpl(
        ContaRecebimentoRepository contaRecebimentoRepository,
        ContaRecebimentoMapper contaRecebimentoMapper
    ) {
        this.contaRecebimentoRepository = contaRecebimentoRepository;
        this.contaRecebimentoMapper = contaRecebimentoMapper;
    }

    @Override
    public ContaRecebimentoDTO save(ContaRecebimentoDTO contaRecebimentoDTO) {
        log.debug("Request to save ContaRecebimento : {}", contaRecebimentoDTO);
        ContaRecebimento contaRecebimento = contaRecebimentoMapper.toEntity(contaRecebimentoDTO);
        contaRecebimento = contaRecebimentoRepository.save(contaRecebimento);
        return contaRecebimentoMapper.toDto(contaRecebimento);
    }

    @Override
    public ContaRecebimentoDTO update(ContaRecebimentoDTO contaRecebimentoDTO) {
        log.debug("Request to update ContaRecebimento : {}", contaRecebimentoDTO);
        ContaRecebimento contaRecebimento = contaRecebimentoMapper.toEntity(contaRecebimentoDTO);
        contaRecebimento = contaRecebimentoRepository.save(contaRecebimento);
        return contaRecebimentoMapper.toDto(contaRecebimento);
    }

    @Override
    public Optional<ContaRecebimentoDTO> partialUpdate(ContaRecebimentoDTO contaRecebimentoDTO) {
        log.debug("Request to partially update ContaRecebimento : {}", contaRecebimentoDTO);

        return contaRecebimentoRepository
            .findById(contaRecebimentoDTO.getId())
            .map(existingContaRecebimento -> {
                contaRecebimentoMapper.partialUpdate(existingContaRecebimento, contaRecebimentoDTO);

                return existingContaRecebimento;
            })
            .map(contaRecebimentoRepository::save)
            .map(contaRecebimentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContaRecebimentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContaRecebimentos");
        return contaRecebimentoRepository.findAll(pageable).map(contaRecebimentoMapper::toDto);
    }

    public Page<ContaRecebimentoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contaRecebimentoRepository.findAllWithEagerRelationships(pageable).map(contaRecebimentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContaRecebimentoDTO> findOne(Long id) {
        log.debug("Request to get ContaRecebimento : {}", id);
        return contaRecebimentoRepository.findOneWithEagerRelationships(id).map(contaRecebimentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContaRecebimento : {}", id);
        contaRecebimentoRepository.deleteById(id);
    }
}
