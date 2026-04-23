describe('E2E flow - user → admin → user', () => {

  it('user sees his article after admin approval', () => {

    const unique = Date.now();
    const gameName = `Game-${unique}`;

    // 👤 USER crée article
    cy.visit('/signin');
    cy.get('input[placeholder="Username"]').type('usertest');
    cy.get('input[placeholder="Password"]').type('Password123!');
    cy.get('input[value="Login"]').click();

    cy.contains('New').click();

    cy.get('input[name="gameName"]').type(gameName);
    cy.get('input[name="articleName"]').type('My Story');
    cy.get('.ProseMirror').click().type('Amazing game');

    cy.contains('Create').click();

    // 👉 logout user
    cy.contains('Logout').click();
    cy.contains('SignIn').click();
    // 👑 ADMIN valide
    cy.get('input[placeholder="Username"]').type('admin');
    cy.get('input[placeholder="Password"]').type('admin');
    cy.get('input[value="Login"]').click();

    cy.contains('Review').click();
    cy.contains('Start Review').click();
    cy.contains('Approve').click();

    // 👉 logout admin
    cy.contains('Logout').click();
    cy.contains('SignIn').click();
    // 👤 USER revient voir Home
    cy.get('input[placeholder="Username"]').type('usertest');
    cy.get('input[placeholder="Password"]').type('Password123!');
    cy.get('input[value="Login"]').click();

    cy.contains('Home').click();

    // ✅ vérification finale
    cy.contains(gameName).should('be.visible');
  });

});
