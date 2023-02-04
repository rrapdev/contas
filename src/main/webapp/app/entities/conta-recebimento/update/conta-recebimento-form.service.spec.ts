import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../conta-recebimento.test-samples';

import { ContaRecebimentoFormService } from './conta-recebimento-form.service';

describe('ContaRecebimento Form Service', () => {
  let service: ContaRecebimentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContaRecebimentoFormService);
  });

  describe('Service methods', () => {
    describe('createContaRecebimentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContaRecebimentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataVencimento: expect.any(Object),
            descricao: expect.any(Object),
            valor: expect.any(Object),
            dataRecebimento: expect.any(Object),
            valorRecebido: expect.any(Object),
            observacoes: expect.any(Object),
            periodicidade: expect.any(Object),
            pagador: expect.any(Object),
            categoriaRecebimento: expect.any(Object),
            caixa: expect.any(Object),
          })
        );
      });

      it('passing IContaRecebimento should create a new form with FormGroup', () => {
        const formGroup = service.createContaRecebimentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataVencimento: expect.any(Object),
            descricao: expect.any(Object),
            valor: expect.any(Object),
            dataRecebimento: expect.any(Object),
            valorRecebido: expect.any(Object),
            observacoes: expect.any(Object),
            periodicidade: expect.any(Object),
            pagador: expect.any(Object),
            categoriaRecebimento: expect.any(Object),
            caixa: expect.any(Object),
          })
        );
      });
    });

    describe('getContaRecebimento', () => {
      it('should return NewContaRecebimento for default ContaRecebimento initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContaRecebimentoFormGroup(sampleWithNewData);

        const contaRecebimento = service.getContaRecebimento(formGroup) as any;

        expect(contaRecebimento).toMatchObject(sampleWithNewData);
      });

      it('should return NewContaRecebimento for empty ContaRecebimento initial value', () => {
        const formGroup = service.createContaRecebimentoFormGroup();

        const contaRecebimento = service.getContaRecebimento(formGroup) as any;

        expect(contaRecebimento).toMatchObject({});
      });

      it('should return IContaRecebimento', () => {
        const formGroup = service.createContaRecebimentoFormGroup(sampleWithRequiredData);

        const contaRecebimento = service.getContaRecebimento(formGroup) as any;

        expect(contaRecebimento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContaRecebimento should not enable id FormControl', () => {
        const formGroup = service.createContaRecebimentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContaRecebimento should disable id FormControl', () => {
        const formGroup = service.createContaRecebimentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
