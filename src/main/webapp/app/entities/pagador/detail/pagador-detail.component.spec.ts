import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PagadorDetailComponent } from './pagador-detail.component';

describe('Pagador Management Detail Component', () => {
  let comp: PagadorDetailComponent;
  let fixture: ComponentFixture<PagadorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PagadorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pagador: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PagadorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PagadorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pagador on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pagador).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
