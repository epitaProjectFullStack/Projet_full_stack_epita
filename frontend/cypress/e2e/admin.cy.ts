describe('Page Admin', () => {
  beforeEach(() => {
    cy.visit('/signin');
    cy.intercept('POST', '**/api/auth**').as('loginRequest');
    cy.get('input[placeholder="Username"]').type('admin');
    cy.get('input[placeholder="Password"]').type('admin');
    cy.get('input[value="Login"]').click();
    cy.wait('@loginRequest');
    cy.visit('/admin');
  });

  it('affiche le dashboard admin', () => {
    cy.contains('Admin Dashboard').should('be.visible');
  });

  it('affiche la liste des users', () => {
    cy.contains('Users').should('be.visible');
  });

  it('affiche le bouton Ban', () => {
    cy.contains('Ban').should('be.visible');
  });

  it('un user non admin ne peut pas accéder à /admin', () => {
    cy.visit('/signin');
    cy.get('input[placeholder="Username"]').type('usertest');
    cy.get('input[placeholder="Password"]').type('Password123!');
    cy.get('input[value="Login"]').click();
    cy.visit('/admin');
    cy.contains('SignIn');
  });
});
