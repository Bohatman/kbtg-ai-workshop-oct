import React, { useState } from "react"

interface FormData {
    nameOnCard: string
    cardNumber: string
    cvv: string
    month: string
    year: string
    comments: string
    sameAsShipping: boolean
}

interface ChangeEvent extends React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement> {}

export default function PaymentForm() {
    const [formData, setFormData] = useState<FormData>({
        nameOnCard: "John Doe",
        cardNumber: "1234 5678 9012 3456",
        cvv: "123",
        month: "MM",
        year: "YYYY",
        comments: "",
        sameAsShipping: true
    })

    const [isSubmitting, setIsSubmitting] = useState<boolean>(false)

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsSubmitting(true)
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 2000))
        
        console.log("Payment submitted:", { ...formData })
        setIsSubmitting(false)
    }

    const handleCancel = (): void => {
        console.log("Payment cancelled")
        // ถ้าต้องการ reset ค่า ให้ทำแบบนี้:
        // setFormData({
        //   nameOnCard: '',
        //   cardNumber: '',
        //   cvv: '',
        //   month: 'MM',
        //   year: 'YYYY',
        //   comments: '',
        //   sameAsShipping: true
        // })
    }

    const handleInputChange = (e: ChangeEvent): void => {
        const { name, value, type } = e.target
        const checked = (e.target as HTMLInputElement).checked
        
        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value
        }))
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center p-4">
            <div className="bg-gradient-to-br from-gray-900 to-black text-white p-8 rounded-2xl max-w-md mx-auto shadow-2xl border border-gray-800 backdrop-blur-sm transform transition-all duration-500 hover:scale-[1.02] hover:shadow-purple-500/20">
                {/* Glowing border effect */}
                <div className="absolute inset-0 bg-gradient-to-r from-purple-500/20 to-blue-500/20 rounded-2xl blur-xl -z-10 opacity-0 hover:opacity-100 transition-opacity duration-500"></div>
                
                <form onSubmit={handleSubmit} className="relative">
                    {/* Header with animation */}
                    <div className="mb-8 text-center transform transition-all duration-700 animate-fade-in">
                        <div className="w-16 h-16 bg-gradient-to-r from-purple-500 to-blue-500 rounded-full mx-auto mb-4 flex items-center justify-center shadow-lg transform transition-transform duration-300 hover:rotate-12 hover:scale-110">
                            <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                            </svg>
                        </div>
                        <h2 className="text-2xl font-bold mb-2 bg-gradient-to-r from-purple-400 to-blue-400 bg-clip-text text-transparent">
                            Payment Method
                        </h2>
                        <p className="text-gray-400 text-sm">
                            All transactions are secure and encrypted
                        </p>
                    </div>

                    {/* Name on Card with enhanced styling */}
                    <div className="mb-6 transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.1s'}}>
                        <label className="block text-sm font-medium mb-3 text-gray-300">
                            Name on Card
                        </label>
                        <div className="relative group">
                            <input
                                type="text"
                                name="nameOnCard"
                                value={formData.nameOnCard}
                                onChange={handleInputChange}
                                className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-4 text-white placeholder-gray-500 focus:border-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500/20 transition-all duration-300 backdrop-blur-sm hover:bg-gray-800/70 hover:border-gray-600 focus:scale-[1.02]"
                                placeholder="John Doe"
                            />
                            <div className="absolute inset-0 bg-gradient-to-r from-purple-500/10 to-blue-500/10 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                        </div>
                    </div>

                    {/* Card Number and CVV with stagger animation */}
                    <div className="grid grid-cols-3 gap-4 mb-4">
                        <div className="col-span-2 transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.2s'}}>
                            <label className="block text-sm font-medium mb-3 text-gray-300">
                                Card Number
                            </label>
                            <div className="relative group">
                                <input
                                    type="text"
                                    name="cardNumber"
                                    value={formData.cardNumber}
                                    onChange={handleInputChange}
                                    className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-4 text-white placeholder-gray-500 focus:border-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500/20 transition-all duration-300 backdrop-blur-sm hover:bg-gray-800/70 hover:border-gray-600 focus:scale-[1.02]"
                                    placeholder="1234 5678 9012 3456"
                                />
                                <div className="absolute inset-0 bg-gradient-to-r from-purple-500/10 to-blue-500/10 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                            </div>
                        </div>
                        <div className="transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.3s'}}>
                            <label className="block text-sm font-medium mb-3 text-gray-300">
                                CVV
                            </label>
                            <div className="relative group">
                                <input
                                    type="text"
                                    name="cvv"
                                    value={formData.cvv}
                                    onChange={handleInputChange}
                                    className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-4 text-white placeholder-gray-500 focus:border-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500/20 transition-all duration-300 backdrop-blur-sm hover:bg-gray-800/70 hover:border-gray-600 focus:scale-[1.02]"
                                    placeholder="123"
                                    maxLength={3}
                                />
                                <div className="absolute inset-0 bg-gradient-to-r from-purple-500/10 to-blue-500/10 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                            </div>
                        </div>
                    </div>

                    <p className="text-gray-500 text-xs mb-6 transform transition-all duration-500 animate-fade-in" style={{animationDelay: '0.4s'}}>
                        Enter your 16-digit number.
                    </p>

                    {/* Month and Year with enhanced dropdowns */}
                    <div className="grid grid-cols-2 gap-4 mb-8">
                        <div className="transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.5s'}}>
                            <label className="block text-sm font-medium mb-3 text-gray-300">
                                Month
                            </label>
                            <div className="relative group">
                                <select
                                    name="month"
                                    value={formData.month}
                                    onChange={handleInputChange}
                                    className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-4 text-white focus:border-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500/20 appearance-none transition-all duration-300 backdrop-blur-sm hover:bg-gray-800/70 hover:border-gray-600 focus:scale-[1.02] cursor-pointer"
                                >
                                    <option value="MM">MM</option>
                                    <option value="01">01</option>
                                    <option value="02">02</option>
                                    <option value="03">03</option>
                                    <option value="04">04</option>
                                    <option value="05">05</option>
                                    <option value="06">06</option>
                                    <option value="07">07</option>
                                    <option value="08">08</option>
                                    <option value="09">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                </select>
                                <div className="absolute inset-y-0 right-0 flex items-center pr-4 pointer-events-none">
                                    <svg className="w-5 h-5 text-gray-400 group-hover:text-gray-300 transition-colors duration-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                    </svg>
                                </div>
                                <div className="absolute inset-0 bg-gradient-to-r from-purple-500/10 to-blue-500/10 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                            </div>
                        </div>
                        <div className="transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.6s'}}>
                            <label className="block text-sm font-medium mb-3 text-gray-300">
                                Year
                            </label>
                            <div className="relative group">
                                <select
                                    name="year"
                                    value={formData.year}
                                    onChange={handleInputChange}
                                    className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-4 text-white focus:border-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500/20 appearance-none transition-all duration-300 backdrop-blur-sm hover:bg-gray-800/70 hover:border-gray-600 focus:scale-[1.02] cursor-pointer"
                                >
                                    <option value="YYYY">YYYY</option>
                                    <option value="2024">2024</option>
                                    <option value="2025">2025</option>
                                    <option value="2026">2026</option>
                                    <option value="2027">2027</option>
                                    <option value="2028">2028</option>
                                    <option value="2029">2029</option>
                                </select>
                                <div className="absolute inset-y-0 right-0 flex items-center pr-4 pointer-events-none">
                                    <svg className="w-5 h-5 text-gray-400 group-hover:text-gray-300 transition-colors duration-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                    </svg>
                                </div>
                                <div className="absolute inset-0 bg-gradient-to-r from-purple-500/10 to-blue-500/10 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                            </div>
                        </div>
                    </div>

                    {/* Billing Address with enhanced styling */}
                    <div className="mb-6 transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.7s'}}>
                        <h3 className="text-lg font-semibold mb-2 text-gray-200">
                            Billing Address
                        </h3>
                        <p className="text-gray-400 text-sm mb-4">
                            The billing address associated with your payment method
                        </p>

                        <div className="flex items-center group cursor-pointer p-2 rounded-lg hover:bg-gray-800/30 transition-colors duration-300">
                            <div className="relative">
                                <input
                                    type="checkbox"
                                    name="sameAsShipping"
                                    checked={formData.sameAsShipping}
                                    onChange={handleInputChange}
                                    className="w-5 h-5 bg-transparent border-2 border-purple-500 rounded focus:ring-purple-500 focus:ring-2 checked:bg-purple-500 checked:border-purple-500 transition-all duration-300 cursor-pointer"
                                />
                                {formData.sameAsShipping && (
                                    <svg className="w-3 h-3 text-white absolute top-0.5 left-0.5 pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                                    </svg>
                                )}
                            </div>
                            <label className="ml-3 text-sm text-gray-300 group-hover:text-white transition-colors duration-300 cursor-pointer">
                                Same as shipping address
                            </label>
                        </div>
                    </div>

                    {/* Comments with enhanced textarea */}
                    <div className="mb-8 transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.8s'}}>
                        <label className="block text-sm font-medium mb-3 text-gray-300">
                            Comments
                        </label>
                        <div className="relative group">
                            <textarea
                                name="comments"
                                value={formData.comments}
                                onChange={handleInputChange}
                                rows={4}
                                className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-4 text-white placeholder-gray-500 focus:border-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500/20 resize-none transition-all duration-300 backdrop-blur-sm hover:bg-gray-800/70 hover:border-gray-600 focus:scale-[1.02]"
                                placeholder="Add any additional comments"
                            />
                            <div className="absolute inset-0 bg-gradient-to-r from-purple-500/10 to-blue-500/10 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                        </div>
                    </div>

                    {/* Enhanced Buttons with loading state */}
                    <div className="flex gap-4 transform transition-all duration-500 animate-slide-up" style={{animationDelay: '0.9s'}}>
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="flex-1 bg-gradient-to-r from-purple-600 to-blue-600 hover:from-purple-700 hover:to-blue-700 disabled:from-gray-600 disabled:to-gray-700 text-white font-semibold py-4 px-6 rounded-xl transition-all duration-300 transform hover:scale-105 hover:shadow-lg hover:shadow-purple-500/25 active:scale-95 disabled:cursor-not-allowed disabled:transform-none flex items-center justify-center"
                        >
                            {isSubmitting ? (
                                <>
                                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
                                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                    </svg>
                                    Processing...
                                </>
                            ) : (
                                'Submit Payment'
                            )}
                        </button>
                        <button
                            type="button"
                            onClick={handleCancel}
                            disabled={isSubmitting}
                            className="flex-1 bg-gray-700 hover:bg-gray-600 disabled:bg-gray-800 text-white font-semibold py-4 px-6 rounded-xl transition-all duration-300 transform hover:scale-105 hover:shadow-lg active:scale-95 border border-gray-600 hover:border-gray-500 disabled:cursor-not-allowed disabled:transform-none"
                        >
                            Cancel
                        </button>
                    </div>
                </form>

                {/* Decorative elements */}
                <div className="absolute -top-2 -right-2 w-20 h-20 bg-gradient-to-br from-purple-500/20 to-blue-500/20 rounded-full blur-xl opacity-70"></div>
                <div className="absolute -bottom-2 -left-2 w-16 h-16 bg-gradient-to-tr from-blue-500/20 to-purple-500/20 rounded-full blur-xl opacity-70"></div>
            </div>
        </div>
    )
}
