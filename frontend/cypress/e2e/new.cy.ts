describe('New Page', () => {
  beforeEach(() => {
    // La page /new nécessite d'être connecté
    cy.visit('/signin');
    cy.get('input[placeholder="Username"]').type('admin');
    cy.get('input[placeholder="Password"]').type('admin');
    cy.get('input[value="Login"]').click();
    cy.url().should('include', '/');
    cy.contains('Logout').should('be.visible');
    cy.contains('New').click();
  });
  it('load new page', () => {
    cy.contains('New').should('be.visible');
  });
  it('should not create article with empty fields', () => {
    cy.get('input[value=Create').click();
    cy.url().should('include', '/new');
  });
});
