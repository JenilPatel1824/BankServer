package server;

import service.BankService;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable
{
    private final Socket socket;

    private final BankService bankService;

    public ClientHandler(Socket socket, BankService bankService)
    {
        this.socket = socket;

        this.bankService = bankService;
    }

    @Override
    public void run()
    {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()))
        {

            while (true)
            {
                String request = (String) input.readObject();

                if ("exit".equalsIgnoreCase(request))
                    break;

                String[] parts = request.split(" ");

                long userId = Long.parseLong(parts[1]);

                long accountNumber = Long.parseLong(parts[2]);

                String response;

                switch (parts[0].toUpperCase())
                {
                    case "CHECK":

                        double balance = bankService.checkBalance(userId, accountNumber);

                        if(balance==-1)
                        {
                            response = "Verification failed!!";
                        }
                        else
                        {
                            response = "Balance: "+balance;
                        }
                        break;

                    case "DEPOSIT":

                        double depositAmount = Double.parseDouble(parts[3]);

                        if(depositAmount==-1)
                        {
                            response = "Verification failed!!";
                        }
                        else
                        {
                            response = "New Balance: " + bankService.deposit(userId, accountNumber, depositAmount);
                        }
                        break;

                    case "WITHDRAW":

                        double withdrawAmount = Double.parseDouble(parts[3]);

                        if(withdrawAmount==-1)
                        {
                            response = "Verification failed!!";
                        }
                        else
                        {
                            response = "New Balance: " + bankService.withdraw(userId, accountNumber, withdrawAmount);
                        }
                        break;

                    default:
                        response = "Invalid Command";
                }
                output.writeObject(response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
