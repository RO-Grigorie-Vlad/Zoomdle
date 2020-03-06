import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { AplicareLicentaDetailComponent } from 'app/entities/aplicare-licenta/aplicare-licenta-detail.component';
import { AplicareLicenta } from 'app/shared/model/aplicare-licenta.model';

describe('Component Tests', () => {
  describe('AplicareLicenta Management Detail Component', () => {
    let comp: AplicareLicentaDetailComponent;
    let fixture: ComponentFixture<AplicareLicentaDetailComponent>;
    const route = ({ data: of({ aplicareLicenta: new AplicareLicenta(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [AplicareLicentaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AplicareLicentaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AplicareLicentaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aplicareLicenta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aplicareLicenta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
