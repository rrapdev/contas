import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../conta-pagamento.test-samples';

import { ContaPagamentoFormService } from './conta-pagamento-form.service';

describe('ContaPagamento Form Service', () => {
  let service: ContaPagamentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContaPagamentoFormService);
  });

  describe('Service methods', () => {
    describe('createContaPagamentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContaPagamentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataVencimento: expect.any(Object),
            descricao: expect.any(Object),
            valor: expect.any(Object),
            dataPagamento: expect.any(Object),
            valorPago: expect.any(Object),
            observacoes: expect.any(Object),
            periodicidade: expect.any(Object),
            beneficiario: expect.any(Object),
            categoriaPagamento: expect.any(Object),
            caixa: expect.any(Object),
          })
        );
      });

      it('passing IContaPagamento should create a new form with FormGroup', () => {
        const formGroup = service.createContaPagamentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataVencimento: expect.any(Object),
            descricao: expect.any(Object),
            valor: expect.any(Object),
            dataPagamento: expect.any(Object),
            valorPago: expect.any(Object),
            observacoes: expect.any(Object),
            periodicidade: expect.any(Object),
            beneficiario: expect.any(Object),
            categoriaPagamento: expect.any(Object),
            caixa: expect.any(Object),
          })
        );
      });
    });

    describe('getContaPagamento', () => {
      it('should return NewContaPagamento for default ContaPagamento initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContaPagamentoFormGroup(sampleWithNewData);

        const contaPagamento = service.getContaPagamento(formGroup) as any;

        expect(contaPagamento).toMatchObject(sampleWithNewData);
      });

      it('should return NewContaPagamento for empty ContaPagamento initial value', () => {
        const formGroup = service.createContaPagamentoFormGroup();

        const contaPagamento = service.getContaPagamento(formGroup) as any;

        expect(contaPagamento).toMatchObject({});
      });

      it('should return IContaPagamento', () => {
        const formGroup = service.createContaPagamentoFormGroup(sampleWithRequiredData);

        const contaPagamento = service.getContaPagamento(formGroup) as any;

        expect(contaPagamento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContaPagamento should not enable id FormControl', () => {
        const formGroup = service.createContaPagamentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContaPagamento should disable id FormControl', () => {
        const formGroup = service.createContaPagamentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
