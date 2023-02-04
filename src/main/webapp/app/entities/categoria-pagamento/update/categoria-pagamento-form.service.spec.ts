import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../categoria-pagamento.test-samples';

import { CategoriaPagamentoFormService } from './categoria-pagamento-form.service';

describe('CategoriaPagamento Form Service', () => {
  let service: CategoriaPagamentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoriaPagamentoFormService);
  });

  describe('Service methods', () => {
    describe('createCategoriaPagamentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCategoriaPagamentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeCategoria: expect.any(Object),
          })
        );
      });

      it('passing ICategoriaPagamento should create a new form with FormGroup', () => {
        const formGroup = service.createCategoriaPagamentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomeCategoria: expect.any(Object),
          })
        );
      });
    });

    describe('getCategoriaPagamento', () => {
      it('should return NewCategoriaPagamento for default CategoriaPagamento initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCategoriaPagamentoFormGroup(sampleWithNewData);

        const categoriaPagamento = service.getCategoriaPagamento(formGroup) as any;

        expect(categoriaPagamento).toMatchObject(sampleWithNewData);
      });

      it('should return NewCategoriaPagamento for empty CategoriaPagamento initial value', () => {
        const formGroup = service.createCategoriaPagamentoFormGroup();

        const categoriaPagamento = service.getCategoriaPagamento(formGroup) as any;

        expect(categoriaPagamento).toMatchObject({});
      });

      it('should return ICategoriaPagamento', () => {
        const formGroup = service.createCategoriaPagamentoFormGroup(sampleWithRequiredData);

        const categoriaPagamento = service.getCategoriaPagamento(formGroup) as any;

        expect(categoriaPagamento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICategoriaPagamento should not enable id FormControl', () => {
        const formGroup = service.createCategoriaPagamentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCategoriaPagamento should disable id FormControl', () => {
        const formGroup = service.createCategoriaPagamentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
