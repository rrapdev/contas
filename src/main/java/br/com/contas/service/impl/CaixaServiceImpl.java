package br.com.contas.service.impl;

import br.com.contas.domain.Caixa;
import br.com.contas.repository.CaixaRepository;
import br.com.contas.service.CaixaService;
import br.com.contas.service.dto.CaixaDTO;
import br.com.contas.service.mapper.CaixaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Caixa}.
 */
@Service
@Transactional
public class CaixaServiceImpl implements CaixaService {

    private final Logger log = LoggerFactory.getLogger(CaixaServiceImpl.class);

    private final CaixaRepository caixaRepository;

    private final CaixaMapper caixaMapper;

    public CaixaServiceImpl(CaixaRepository caixaRepository, CaixaMapper caixaMapper) {
        this.caixaRepository = caixaRepository;
        this.caixaMapper = caixaMapper;
    }

    @Override
    public CaixaDTO save(CaixaDTO caixaDTO) {
        log.debug("Request to save Caixa : {}", caixaDTO);
        Caixa caixa = caixaMapper.toEntity(caixaDTO);
        caixa = caixaRepository.save(caixa);
        return caixaMapper.toDto(caixa);
    }

    @Override
    public CaixaDTO update(CaixaDTO caixaDTO) {
        log.debug("Request to update Caixa : {}", caixaDTO);
        Caixa caixa = caixaMapper.toEntity(caixaDTO);
        caixa = caixaRepository.save(caixa);
        return caixaMapper.toDto(caixa);
    }

    @Override
    public Optional<CaixaDTO> partialUpdate(CaixaDTO caixaDTO) {
        log.debug("Request to partially update Caixa : {}", caixaDTO);

        return caixaRepository
            .findById(caixaDTO.getId())
            .map(existingCaixa -> {
                caixaMapper.partialUpdate(existingCaixa, caixaDTO);

                return existingCaixa;
            })
            .map(caixaRepository::save)
            .map(caixaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CaixaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Caixas");
        return caixaRepository.findAll(pageable).map(caixaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CaixaDTO> findOne(Long id) {
        log.debug("Request to get Caixa : {}", id);
        return caixaRepository.findById(id).map(caixaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caixa : {}", id);
        caixaRepository.deleteById(id);
    }
}
