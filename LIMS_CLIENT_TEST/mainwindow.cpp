#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    commModule = new CommModule();
    jo.insert("USERNAME",QString("2013010918015 "));
    jo.insert("PASSWORD",QString("8682502101"));
    connect(ui->sendBtn,SIGNAL(clicked(bool)),this,SLOT(sendMsg()));
    connect(commModule,SIGNAL(getResponse(QByteArray)),this,SLOT(getMsg(QByteArray)));
}

void MainWindow::sendMsg()
{
    QJsonDocument jd;
    jd.setObject(jo);
    QString msg = QString(jd.toJson());
    ui->textEdit->append("send:");
    ui->textEdit->append(msg);
    commModule->sendMessage(jd.toJson());
}

void MainWindow::getMsg(QByteArray resp)
{
    QString s = QString(resp);
    ui->textEdit->append("receive:");
    ui->textEdit->append(s);
}

MainWindow::~MainWindow()
{
    delete ui;
}
