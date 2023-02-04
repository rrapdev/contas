import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IContaPagamento } from '../conta-pagamento.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../conta-pagamento.test-samples';

import { ContaPagamentoService, RestContaPagamento } from './conta-pagamento.service';

const requireRestSample: RestContaPagamento = {
  ...sampleWithRequiredData,
  dataVencimento: sampleWithRequiredData.dataVencimento?.format(DATE_FORMAT),
  dataPagamento: sampleWithRequiredData.dataPagamento?.format(DATE_FORMAT),
};

describe('ContaPagamento Service', () => {
  let service: ContaPagamentoService;
  let httpMock: HttpTestingController;
  let expectedResult: IContaPagamento | IContaPagamento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContaPagamentoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ContaPagamento', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const contaPagamento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contaPagamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContaPagamento', () => {
      const contaPagamento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contaPagamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContaPagamento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContaPagamento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContaPagamento', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContaPagamentoToCollectionIfMissing', () => {
      it('should add a ContaPagamento to an empty array', () => {
        const contaPagamento: IContaPagamento = sampleWithRequiredData;
        expectedResult = service.addContaPagamentoToCollectionIfMissing([], contaPagamento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contaPagamento);
      });

      it('should not add a ContaPagamento to an array that contains it', () => {
        const contaPagamento: IContaPagamento = sampleWithRequiredData;
        const contaPagamentoCollection: IContaPagamento[] = [
          {
            ...contaPagamento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContaPagamentoToCollectionIfMissing(contaPagamentoCollection, contaPagamento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContaPagamento to an array that doesn't contain it", () => {
        const contaPagamento: IContaPagamento = sampleWithRequiredData;
        const contaPagamentoCollection: IContaPagamento[] = [sampleWithPartialData];
        expectedResult = service.addContaPagamentoToCollectionIfMissing(contaPagamentoCollection, contaPagamento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contaPagamento);
      });

      it('should add only unique ContaPagamento to an array', () => {
        const contaPagamentoArray: IContaPagamento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contaPagamentoCollection: IContaPagamento[] = [sampleWithRequiredData];
        expectedResult = service.addContaPagamentoToCollectionIfMissing(contaPagamentoCollection, ...contaPagamentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contaPagamento: IContaPagamento = sampleWithRequiredData;
        const contaPagamento2: IContaPagamento = sampleWithPartialData;
        expectedResult = service.addContaPagamentoToCollectionIfMissing([], contaPagamento, contaPagamento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contaPagamento);
        expect(expectedResult).toContain(contaPagamento2);
      });

      it('should accept null and undefined values', () => {
        const contaPagamento: IContaPagamento = sampleWithRequiredData;
        expectedResult = service.addContaPagamentoToCollectionIfMissing([], null, contaPagamento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contaPagamento);
      });

      it('should return initial array if no ContaPagamento is added', () => {
        const contaPagamentoCollection: IContaPagamento[] = [sampleWithRequiredData];
        expectedResult = service.addContaPagamentoToCollectionIfMissing(contaPagamentoCollection, undefined, null);
        expect(expectedResult).toEqual(contaPagamentoCollection);
      });
    });

    describe('compareContaPagamento', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContaPagamento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContaPagamento(entity1, entity2);
        const compareResult2 = service.compareContaPagamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContaPagamento(entity1, entity2);
        const compareResult2 = service.compareContaPagamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContaPagamento(entity1, entity2);
        const compareResult2 = service.compareContaPagamento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
