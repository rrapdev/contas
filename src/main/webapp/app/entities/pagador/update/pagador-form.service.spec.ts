import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pagador.test-samples';

import { PagadorFormService } from './pagador-form.service';

describe('Pagador Form Service', () => {
  let service: PagadorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PagadorFormService);
  });

  describe('Service methods', () => {
    describe('createPagadorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPagadorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomePagador: expect.any(Object),
            cpfCnpj: expect.any(Object),
          })
        );
      });

      it('passing IPagador should create a new form with FormGroup', () => {
        const formGroup = service.createPagadorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomePagador: expect.any(Object),
            cpfCnpj: expect.any(Object),
          })
        );
      });
    });

    describe('getPagador', () => {
      it('should return NewPagador for default Pagador initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPagadorFormGroup(sampleWithNewData);

        const pagador = service.getPagador(formGroup) as any;

        expect(pagador).toMatchObject(sampleWithNewData);
      });

      it('should return NewPagador for empty Pagador initial value', () => {
        const formGroup = service.createPagadorFormGroup();

        const pagador = service.getPagador(formGroup) as any;

        expect(pagador).toMatchObject({});
      });

      it('should return IPagador', () => {
        const formGroup = service.createPagadorFormGroup(sampleWithRequiredData);

        const pagador = service.getPagador(formGroup) as any;

        expect(pagador).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPagador should not enable id FormControl', () => {
        const formGroup = service.createPagadorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPagador should disable id FormControl', () => {
        const formGroup = service.createPagadorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
