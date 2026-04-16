import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewerBoard } from './reviewer-board';

describe('ReviewerBoard', () => {
  let component: ReviewerBoard;
  let fixture: ComponentFixture<ReviewerBoard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewerBoard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewerBoard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
