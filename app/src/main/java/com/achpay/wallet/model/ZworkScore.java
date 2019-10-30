package com.achpay.wallet.model;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class ZworkScore extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610c8c380380610c8c833981016040908152815160208084015183850151606086015160ff8216600a0a850260038190553360009081526005865296872055918601805194969095919492019261006e929086019061009b565b506001805460ff191660ff8416179055805161009190600290602084019061009b565b5050505050610136565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100dc57805160ff1916838001178555610109565b82800160010185558215610109579182015b828111156101095782518255916020019190600101906100ee565b50610115929150610119565b5090565b61013391905b80821115610115576000815560010161011f565b90565b610b47806101456000396000f3006080604052600436106100b95763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde0381146100be57806318160ddd14610148578063313ce5671461016f5780633d1aa70a1461019a57806342966c68146101f357806367a09c231461020d57806370a082311461024557806395d89b4114610266578063a0712d681461027b578063cb828b7a14610293578063d68e462c146102ee578063f3db9fe514610312575b600080fd5b3480156100ca57600080fd5b506100d361037b565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561010d5781810151838201526020016100f5565b50505050905090810190601f16801561013a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561015457600080fd5b5061015d610409565b60408051918252519081900360200190f35b34801561017b57600080fd5b5061018461040f565b6040805160ff9092168252519081900360200190f35b3480156101a657600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261015d9436949293602493928401919081908401838280828437509497506104189650505050505050565b3480156101ff57600080fd5b5061020b600435610480565b005b34801561021957600080fd5b50610231600160a060020a03600435166024356104a2565b604080519115158252519081900360200190f35b34801561025157600080fd5b5061015d600160a060020a036004351661059e565b34801561027257600080fd5b506100d36105b9565b34801561028757600080fd5b5061020b600435610611565b34801561029f57600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261023194369492936024939284019190819084018382808284375094975050933594506106319350505050565b3480156102fa57600080fd5b50610231600160a060020a036004351660243561081e565b34801561031e57600080fd5b5060408051602060046024803582810135601f8101859004850286018501909652858552610231958335600160a060020a031695369560449491939091019190819084018382808284375094975050933594506109109350505050565b6000805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156104015780601f106103d657610100808354040283529160200191610401565b820191906000526020600020905b8154815290600101906020018083116103e457829003601f168201915b505050505081565b60035481565b60015460ff1681565b60006004826040518082805190602001908083835b6020831061044c5780518252601f19909201916020918201910161042d565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054949350505050565b3360009081526005602052604090208054829003905560038054919091039055565b3360009081526005602052604081205482118015906104da5750600160a060020a038316600090815260056020526040902054828101115b15156104e557600080fd5b600354600160a060020a0384166000908152600560205260409020548301111561050e57600080fd5b600160a060020a0383161580159061052557503315155b151561053057600080fd5b33600081815260056020908152604080832080548790039055600160a060020a03871680845292819020805487019055805186815290519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a350600192915050565b600160a060020a031660009081526005602052604090205490565b6002805460408051602060018416156101000260001901909316849004601f810184900484028201840190925281815292918301828280156104015780601f106103d657610100808354040283529160200191610401565b336000908152600560205260409020805482019055600380549091019055565b60006004836040518082805190602001908083835b602083106106655780518252601f199092019160209182019101610646565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902054826004856040518082805190602001908083835b602083106106cc5780518252601f1990920191602091820191016106ad565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020540111151561070d57600080fd5b816004846040518082805190602001908083835b602083106107405780518252601f199092019160209182019101610721565b51815160209384036101000a60001901801990921691161790529201948552506040805194859003820185208054969096019095553360009081526005825294909420805487900390555050845185928291908401908083835b602083106107b95780518252601f19909201916020918201910161079a565b51815160209384036101000a60001901801990921691161790526040805192909401829003822088835293519395503394507f83b18b901525b858e499dfb919f0dbbe566e33ad9082ff65c42e634c3d4712d49391829003019150a350600192915050565b600160a060020a0382166000908152600560205260408120548211801590610856575033600090815260056020526040902054828101115b151561086157600080fd5b6003543360009081526005602052604090205483011061088057600080fd5b600160a060020a0383161580159061089757503315155b15156108a257600080fd5b336000818152600560209081526040808320805487019055600160a060020a0387168084529281902080548790039055805186815290519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a350600192915050565b6000816004846040518082805190602001908083835b602083106109455780518252601f199092019160209182019101610926565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205410158015610a5257506004836040518082805190602001908083835b602083106109b45780518252601f199092019160209182019101610995565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902054826004856040518082805190602001908083835b60208310610a1b5780518252601f1990920191602091820191016109fc565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205403105b1515610a5d57600080fd5b816004846040518082805190602001908083835b60208310610a905780518252601f199092019160209182019101610a71565b51815160209384036101000a6000190180199092169116179052920194855250604080519485900382018520805496909603909555600160a060020a038916600081815260058352869020805489019055878552945133947fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef94508190039091019150a393925050505600a165627a7a72305820121afaf57a37b0bb39fd2acdddf653fb07e1e125c7356474d75cb6e76d8ed7a90029";

    protected ZworkScore(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ZworkScore(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransfervEventResponse> getTransfervEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transferv", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransfervEventResponse> responses = new ArrayList<TransfervEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransfervEventResponse typedResponse = new TransfervEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransfervEventResponse> transfervEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transferv", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransfervEventResponse>() {
            @Override
            public TransfervEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransfervEventResponse typedResponse = new TransfervEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        final Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function("totalSupply", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function("decimals", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> vm_balanceOf(String _owner) {
        final Function function = new Function("vm_balanceOf", 
                Arrays.<Type>asList(new Utf8String(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger awd) {
        final Function function = new Function(
                "burn", 
                Arrays.<Type>asList(new Uint256(awd)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> payment(String _to, BigInteger _value) {
        final Function function = new Function(
                "payment", 
                Arrays.<Type>asList(new Address(_to), 
                new Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function("symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> mint(BigInteger awd) {
        final Function function = new Function(
                "mint", 
                Arrays.<Type>asList(new Uint256(awd)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> grant(String _to, BigInteger _value) {
        final Function function = new Function(
                "grant", 
                Arrays.<Type>asList(new Utf8String(_to), 
                new Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withhold(String member, BigInteger _value) {
        final Function function = new Function(
                "withhold", 
                Arrays.<Type>asList(new Address(member), 
                new Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> deposit(String from, String vmId, BigInteger _value) {
        final Function function = new Function(
                "deposit", 
                Arrays.<Type>asList(new Address(from), 
                new Utf8String(vmId), 
                new Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<ZworkScore> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialAmount), 
                new Utf8String(_tokenName), 
                new Uint8(_decimalUnits), 
                new Utf8String(_tokenSymbol)));
        return deployRemoteCall(ZworkScore.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<ZworkScore> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialAmount), 
                new Utf8String(_tokenName), 
                new Uint8(_decimalUnits), 
                new Utf8String(_tokenSymbol)));
        return deployRemoteCall(ZworkScore.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static ZworkScore load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ZworkScore(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ZworkScore load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ZworkScore(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class TransfervEventResponse {
        public Log log;

        public String _from;

        public byte[] _to;

        public BigInteger _value;
    }
}
