import { NO_ERRORS_SCHEMA } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderComponent],
      schemas: [NO_ERRORS_SCHEMA],
    });
  });

  function setup() {
    const fixture = TestBed.createComponent(HeaderComponent);
    const component = fixture.componentInstance;

    return { fixture, component };
  }

  it('should create the component', () => {
    const { component } = setup();
    expect(component).toBeTruthy();
  });
});
